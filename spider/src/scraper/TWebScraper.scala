package scraper

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.util.matching.Regex

import crawler.CrawlerTypes._
import common.SpanishMonth._
import common.SpanishMonth
import common.TLogging

trait TWebScraper extends TLogging {

  def apply(
    page: WebPage,
    linkText: String,
    link: URL,
    html: String,
  ): Option[WebArticle]

  protected def parseHTMLDocument(html: String): Document = Jsoup.parse(html)

  /**
    * Attempts to match a spanish month with its numeric representation
    *
    * @param month Month name
    * @return Integer representation of the month
    */
  protected def monthWordToNum(
    m: String,
  ): Int = {
    // Guard against abbreviation dot
    val month = m.toLowerCase.strip.replace(".", "")
    SpanishMonth.withNameOpt(month) getOrElse("") match {
      case SpanishMonth.ENERO => 1
      case SpanishMonth.FEBRERO => 2
      case SpanishMonth.MARZO => 3
      case SpanishMonth.ABRIL => 4
      case SpanishMonth.MAYO => 5
      case SpanishMonth.JUNIO => 6
      case SpanishMonth.JULIO => 7
      case SpanishMonth.AGOSTO => 8
      case SpanishMonth.SEPTIEMBRE => 9
      case SpanishMonth.OCTUBRE => 10
      case SpanishMonth.NOVIEMBRE => 11
      case SpanishMonth.DICIEMBRE => 12
      case _ => -1
    }
  }
  
  val YEAR_PATTERN_REGEX: Regex = "\\d{4,}".r
  val DAY_PATTERN_REGEX: Regex = "[1-9]+".r
  /**
    * Attempt to get an Article Date from a spanish string date
    * i.e. 24 de julio 2020 , 03:30 p.m.
    * 24 de julio de 2020 , 03:30 p.m.
    * etc.
    *
    * @param date String representation of the date
    * @return ArticleDate
    */
  protected def scrapeSpanishDate(
    date: String,
  ): Option[ArticleDate] = {
    val cleanDate = date.strip()
    if (cleanDate.isEmpty) {
      return None
    }
    val parts = cleanDate.split(" ")

    val monthArr = parts
      .map(str => monthWordToNum(str))
      .filter(_ != -1)
    val year = YEAR_PATTERN_REGEX.findFirstIn(cleanDate).getOrElse("")
    val day = DAY_PATTERN_REGEX.findFirstIn(cleanDate).getOrElse("")
    if (monthArr.length != 1 || year.isEmpty || day.isEmpty) {
      return None
    }

    Some( ArticleDate(day.toInt, monthArr(0).toInt, year.toInt) )
  }

  val BASE_URL_REGEX: Regex = "(http(s)?:\\/\\/)|(\\/.*){1}".r
  /**
    * Fetch base URL 
    *
    * @param fullyQualifiedURL i.e https://foo.goo.com/baz/boo
    * @return A string representing the base url domain i.e. foo.goo.com
    */
  protected def getBaseDomain(
    fullyQualifiedURL: URL,
  ): String = BASE_URL_REGEX.replaceAllIn(fullyQualifiedURL, "")
}

