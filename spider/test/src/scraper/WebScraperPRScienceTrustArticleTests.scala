/**
 * This file is partiallty generated. Only make modifications between
 * BEGIN MANUAL SECTION and END MANUAL SECTION designators.
 * 
 * This file is was generated by ./sc scraper script command.
 */
package scraper

import utest._
import scala.io.Source
import common.CountryType._
import common.CountryType
import common.SourceType._
import common.SourceType
import common.TestUtils
import crawler.CrawlerTypes._
/** BEGIN MANUAL SECTION */
/** END MANUAL SECTION */

object WebScraperPRScienceTrustArticleTests extends TestSuite with TestUtils {
  
  var html = ""

  val PRScienceTrustURL = "https://prsciencetrust.org/blog/"
  /** BEGIN MANUAL SECTION */
  /** END MANUAL SECTION */

  override def utestBeforeEach(path: Seq[String]): Unit = {
    if (html.isEmpty) {
      val htmlStream = getClass.getResourceAsStream("/PRScienceTrust.html.test")
      html = Source.fromInputStream(htmlStream).getLines.mkString
    }
  }

  def tests = Tests {
    test("Web Scrape PRScienceTrust") {
      /** END BEGIN SECTION */
      val page =  WebPage(PRScienceTrustURL, CountryType.PUERTORICO, SourceType.PR_SCIENCE_TRUST)
      val linkURL = "https://prsciencetrust.org/recomendaciones-uso-del-agua-2/"
      val linkText = "Recomendaciones Para El Uso Del Agua Ante El Racionamiento Durante La Pandemia De COVID-19"
      val author = Some("María Elena Martínez Hernández")
      val maybeArticle: Option[WebArticle] = WebScraperPRScienceTrustArticle(
        page,
        linkText,
        linkURL,
        html,
      )
      val thumb = "https://prsciencetrust.org/wp-content/uploads/2020/07/Mailchimp-newletter-headers-racionamiento.jpg"
      val date = ArticleDate(22,7,2020)
      maybeArticle match {
        case Some(a: WebArticle) => testArticle(
          a,
          page,
          linkText,
          linkURL,
          thumb,
          date,
          author,
        )
        case _ => assert(false)
      }
      /** END MANUAL SECTION */
    }
  }
  /** BEGIN MANUAL SECTION */
  private def testArticle(
    a: WebArticle,
    origin: WebPage,
    linkText: String,
    linkURL: URL,
    thumb: URL,
    date: ArticleDate,
    author: Option[String],
  ): Unit = {
    assert(a.title == linkText)
    assert(a.origin == origin)
    assert(a.originLinkText == linkText)
    assert(a.fullyQualifiedLink == linkURL)
    assert(checkOptionalMatch(a.image, thumb))
    assert(checkOptionalMatch(a.datePublished, date))
    assert(a.outBoundLinks.size > 0)
    assert(!a.text.contains("<html>"))
    assert(!a.text.contains("<body>"))
    assert(!a.text.contains("<script>"))
    assert(!checkOptionalMatch(a.author, ""))
    a.outBoundLinks.foreach(linkPair => {
      val (text, url) = linkPair
      assert(!text.isEmpty)
      assert(url.startsWith("http"))
    })
  }
  /** END MANUAL SECTION */
}

