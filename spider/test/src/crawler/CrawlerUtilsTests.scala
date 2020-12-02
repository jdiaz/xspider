package crawler

import utest._

import common.CountryType
import common.SourceType
import crawler.CrawlerTypes._

object CrawlerUtilsTests extends TestSuite {

  def tests = Tests {
    test("Get name from www URL") {
      checkGetNameFromURLMatch(
        "https://www.eluniversal.com.mx/techbit",
        "eluniversal.com.mx",
      )
    }
    test("Get name from non standard URL") {
      checkGetNameFromURLMatch(
        "https://cnnespanol.cnn.com/seccion/tecnologia/",
        "cnnespanol.cnn.com",
      )
    }
    test("Get name from malformed URL") {
      checkGetNameFromURLMatch("http:/by.tx.com", "http:/by.tx.com")
    }

    test("webArticleToJSON") {
      val seq = Seq(
        new WebArticle(
          origin = new WebPage("http://origin.com", CountryType.PUERTORICO, SourceType.TEST),
          originLinkText = "mytestlinktext",
          fullyQualifiedLink = "http://mytesturl",
          title = "my test article",
          author = Some("Jose"),
          datePublished =  None,
          outBoundLinks = Set(),
          image = None,
          text = "lorem ipsum. lorem ipsum. lorem ipsum",
        ),
      )
      val json = CrawlerUtils.webArticlesToJSON(seq, -1)
      val expected = 
        """[{"title":"my test article","originLinkText":"mytestlinktext","source":"test","sourceURL":"http://origin.com","country":"Puerto Rico","author":"Jose","date":"","url":"http://mytesturl","textLength":37,"image":"","outBoundLinksCount":0,"outBoundLinks":[[]],"text":"lorem ipsum. lorem ipsum. lorem ipsum"}]"""
      assert(json == expected)
    }
  }

  def checkGetNameFromURLMatch(url: String, expected: String): Unit = {
    val name = CrawlerUtils.getNameFromURL(url)
    assert(name == expected)
  }
}
