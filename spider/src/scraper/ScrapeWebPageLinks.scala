package scraper

import collection.JavaConverters._

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import crawler.CrawlerTypes._
import common.SourceType._
import common.SourceType
import common.TLogging

object ScrapeWebPageLinks extends TLogging {

  def apply(
    siteURL: URL,
    source: SourceType,
    html: String,
  ): Set[LinkPair] = {
    val selector = getSelectorForSource(source)
    val links = parseHTMLDocument(html)
      .select(selector)
      .asScala
      .map { ele: Element =>
        val href = ele.attr("href")
        val url = if (href.startsWith("http")) {
          href
        } else {
          val baseURL = getBaseURL(siteURL)
          val relativeLink = ele.attr("href") // Should start with /
          s"https://www.$baseURL$relativeLink"
        }
        val text = ele.text()

        (text, url)
      }
      .filter(pair => !pair._1.isEmpty && !pair._2.isEmpty)
      .toSet
    
    log.info(
      "Scraped links from page {}: {}",
      Array(source.toString, links.mkString("[\n",",\n\t","\n]")),
    )
    links
  }

  private def getSelectorForSource(
    source: SourceType,
  ): String = source match { 
    case SourceType.EL_NUEVO_DIA 
    | SourceType.EL_PAIS 
    | SourceType.CNN 
      => "article a"
    case SourceType.PR_SCIENCE_TRUST => "h2.blog-shortcode-post-title.entry-title a"
    case SourceType.EL_TIEMPO => ".titulo a"
    case SourceType.BUSINESS_INSIDER => "h2:has(a) a, h3:has(a) a"
    case _ => "a"
  }
 
  private def getBaseURL(
    siteURL: URL,
  ): URL = {
    if (siteURL.contains("www")) {
      siteURL.split("www.")(1).split("/")(0)
    } else {
      siteURL.split("//")(1).split("/")(0)
    }
  }

  private def parseHTMLDocument(html: String): Document = Jsoup.parse(html)
}