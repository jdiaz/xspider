package crawler

import scala.concurrent.Future

import common.CountryType._
import common.SourceType._

object CrawlerTypes {
  type URL = String
  type LinkPair = (String, URL)

  case class ArticleDate(
    day: Int,
    month: Int,
    year: Int,
  )

  case class WebPage(
    url: URL,
    country: CountryType,
    source: SourceType,
  )

  case class WebArticle(
    origin: WebPage,
    originLinkText: String,
    fullyQualifiedLink: URL,
    title: String,
    author: Option[String],
    datePublished: Option[ArticleDate],
    outBoundLinks: Set[LinkPair],
    image: Option[URL],
    text: String, // length of article text.length
  )
}