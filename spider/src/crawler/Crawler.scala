package crawler

import java.lang.InterruptedException
import java.util.concurrent.ExecutionException

import scala.concurrent.Future
import scala.concurrent.Promise

import org.asynchttpclient.Dsl
import org.asynchttpclient.Response
import org.asynchttpclient.RequestBuilder

import CrawlerTypes._
import common.CountryType._
import common.SourceType._
import common.SourceType
import common.TLogging
import common.SystemConfig._
import scraper._
import scala.util.Failure
import scala.util.Success
import scala.concurrent.ExecutionContext

object Crawler extends TLogging {

  val asyncHttpClient = Dsl.asyncHttpClient()

  def crawlAllPagesAsync(
    pages: Set[WebPage],
  ): Future[Set[Option[WebArticle]]] = {
    val allWebArticleSets = pages.map(page => crawlPageAsync(page))
    Future.sequence(allWebArticleSets).map(seq => seq.toSet.flatten) 
  }

  private def crawlPageAsync(
    page: WebPage,
  ): Future[Set[Option[WebArticle]]] = fetchAllLinksInPageAsync(page)
    .flatMap { linkSet =>
      val futArticleSet = for ((text, url) <- linkSet) yield fetchAsync(url)
        .map(_.getResponseBody)
        .map(html => WebScraperSelector(page.source, page, text, url, html))
      Future.sequence(futArticleSet)
    }

  private def fetchAllLinksInPageAsync(
    page: WebPage,
  ): Future[Set[LinkPair]] = fetchAsync(page.url)
    .map(_.getResponseBody)
    .map(html => ScrapeWebPageLinks(page.url, page.source, html))

  def fetchAsync(
    url: URL,
  )(implicit ec: ExecutionContext): Future[Response] = {
    val requestBuilder = new RequestBuilder()
      .addHeader("User-Agent", HTTPConfig.USER_AGENT)
      .setMethod(HTTPConfig.GET)
      .setFollowRedirect(HTTPConfig.FOLLOW_REDIRECT)
      .setRequestTimeout(HTTPConfig.REQUEST_TIMEOUT)
      .setUrl(url)

    val startTime = System.currentTimeMillis()
    log.info("BEGIN HTTP GET: {}", url)
    val listenableFuture = asyncHttpClient
      .prepareRequest(requestBuilder)
      .execute()

    val p = Promise[Response]
    listenableFuture.addListener(() => { 
      try {
        val response = listenableFuture.get();
        val completedTime = (System.currentTimeMillis() - startTime) / 1000
        log.info("END HTTP GET: {}s {}", completedTime, url)
        p.success(response)
	    } catch {
        case e: ExecutionException  => {
          log.error(s"Error ExecutionException $url ", e)
          p.failure(e)
        }
        case e: InterruptedException => {
          log.error(s"Error InterruptedException $url ", e)
          p.failure(e)
        }
    	}
    }, null)
    p.future
  }
}

object HTTPConfig {
  val USER_AGENT =  "XSpider Crawler"
  val SUCCESS = 200
  val GET = "GET"
  val FOLLOW_REDIRECT = true
  val REQUEST_TIMEOUT = 60000
}