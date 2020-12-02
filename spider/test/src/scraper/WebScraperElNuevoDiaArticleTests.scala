package scraper

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

object WebScraperElNuevoDiaArticleTests extends TestSuite with TestUtils {
  
  var html = ""

  val ElNuevoDiaURL = "https://www.elnuevodia.com/tecnologia/"
  /** BEGIN MANUAL SECTION */
  /** END MANUAL SECTION */

  override def utestBeforeEach(path: Seq[String]): Unit = {
    if (html.isEmpty) {
      val htmlStream = getClass.getResourceAsStream("/ElNuevoDia.html.test")
      html = Source.fromInputStream(htmlStream).getLines.mkString
    }
  }

  def tests = Tests {
    test("Web Scrape ElNuevoDia") {
      /** BEGIN MANUAL SECTION */
      val page = WebPage(ElNuevoDiaURL, CountryType.PUERTORICO, SourceType.EL_NUEVO_DIA)
      val linkText = "LinkedIn despedirá a casi 1,000 empleados"
      val linkURL = "https://www.elnuevodia.com/tecnologia/redes-sociales/notas/linkedin-despedira-a-casi-1000-empleados/"
      val maybeArticle: Option[WebArticle] = WebScraperElNuevoDiaArticle(
        page,
        linkText,
        linkURL,
        html,
      )
      val thumb = "https://www.elnuevodia.com/resizer/Hvy4_Xd6SLWtGdbZJVkcXlvn3Zo=/188x163/smart/filters:quality(95):format(png)/cloudfront-us-east-1.images.arcpublishing.com/gfrmedia/UA6FUESYBNFAHF7BROEPLL5GJE.jpg"
      val date = ArticleDate(21,7,2020)
      maybeArticle match {
        case Some(a: WebArticle) => testArticle(
          a,
          page,
          linkText,
          linkURL,
          thumb,
          date,
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