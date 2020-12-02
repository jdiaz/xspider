package spider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

import common.CountryType._
import common.SourceType._
import common.TLogging
import crawler._
import crawler.CrawlerTypes._
import common.SystemConfig
import ujson._

object Main extends TLogging {

  def main(args: Array[String]): Unit = {
    log.info("XSpider live run!")
    log.info("XSpider run: cores {}", SystemConfig.CORES)

    val pages = CrawlerUtils.loadWebPageSourcesFromMemory()

    val startTime = System.currentTimeMillis() / 1000
    val futureArticles = Crawler.crawlAllPagesAsync(pages)

    futureArticles.onComplete({
      case Failure(ex) => log.error("Error crawling:", ex)
      case Success(value) => {
        val endTime = System.currentTimeMillis() / 1000
        log.info("Completed crawl: {}s", endTime - startTime)
        log.info("Total number of web articles: {}", value.size)

        val optWebArticles: Set[Option[WebArticle]] = value

        val webArticles = optWebArticles
          .collect({case Some(a: WebArticle) => a})
          .filter(wa => !wa.title.isEmpty)
        
        log.info("Filtering empty title web articles with title: {}", webArticles.size)

        val dedupedWebArticles = dedupeWebArticleByTitle(webArticles)
        log.info("Deduped Web articles by title: {}", dedupedWebArticles.size)

        val jsonStr = CrawlerUtils.webArticlesToJSON(webArticles)
        log.info(jsonStr)

        val success = CrawlerUtils.writeTextToFile("articles", jsonStr)
        System.exit(if (success) 0 else 1)
      }
    })

  }

  def dedupeWebArticleByTitle(
    articles: Traversable[WebArticle],
  ): Set[WebArticle] = {

    def rec(
      coll: Traversable[WebArticle],
      titlesSeen: Set[String],
      acc: Set[WebArticle],
    ): Set[WebArticle] = if (coll.isEmpty) {
      acc
    } else {
      val curr = coll.head
      if (!titlesSeen.contains(curr.title)) {
        rec(coll.tail, titlesSeen ++ Set(curr.title), acc ++ Set(curr))
      } else {
        rec(coll.tail, titlesSeen, acc)
      }
    }

    rec(articles, Set(), Set())
  }

}