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

object WebScraperElTiempoArticleTests extends TestSuite with TestUtils {
  
  var html = ""

  val elTiempoURL = "https://www.eltiempo.com/tecnosfera/"
  /** BEGIN MANUAL SECTION */
  /** END MANUAL SECTION */

  override def utestBeforeEach(path: Seq[String]): Unit = {
    if (html.isEmpty) {
      val htmlStream = getClass.getResourceAsStream("/ElTiempo.html.test")
      html = Source.fromInputStream(htmlStream).getLines.mkString
    }
  }

  def tests = Tests {
    test("Web Scrape ElTiempo") {
      /** BEGIN MANUAL SECTION */
      val page = WebPage(elTiempoURL, CountryType.COLOMBIA, SourceType.EL_TIEMPO)
      val linkURL = "https://www.eltiempo.com/contenido-comercial/el-valle-se-mueve-con-la-energia-de-celsia-521812"
      val linkText = "El Valle se mueve con la energía de Celsia"
      val maybeArticle: Option[WebArticle] = WebScraperElTiempoArticle(
        page,
        linkText,
        linkURL,
        html,
      )
      maybeArticle match {
        case Some(a: WebArticle) => testArticle(
          a,
          page,
          linkText,
          linkURL,//
          "https://www.eltiempo.com/files/article_main/uploads/2020/07/24/5f1b18e104dc2.jpeg",
          ArticleDate(24,7,2020),
          None,
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
    assert(a.title == "El Valle se mueve con la energía de Celsia")
    assert(a.origin == origin)
    assert(a.originLinkText == linkText)
    assert(a.fullyQualifiedLink == linkURL)
    assert(checkOptionalMatch(a.image, thumb))
    assert(checkOptionalMatch(a.datePublished, date))
    assert(a.outBoundLinks.size > 0)
    assert(a.text.length > 0)
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