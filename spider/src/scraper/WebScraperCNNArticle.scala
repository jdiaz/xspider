/**
 * This file is partiallty generated. Only make modifications between
 * BEGIN MANUAL SECTION and END MANUAL SECTION designators.
 * 
 * This file is was generated by ./sc scraper script command.
 */
package scraper

import collection.JavaConverters._
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import common.CountryType._
import crawler.CrawlerTypes._
/** BEGIN MANUAL SECTION */
/** END MANUAL SECTION */

object WebScraperCNNArticle extends TWebScraper {

  def apply(
    page: WebPage,
    linkText: String,
    link: URL,
    html: String,
  ): Option[WebArticle] = {
    val doc = parseHTMLDocument(html)
    /** BEGIN MANUAL SECTION */
    val maybeAuthor = doc.select("p.news__byline").text
    val author = if (!maybeAuthor.isEmpty) {
      val auth = maybeAuthor
        .replace("Por","")
        .replace("CNN","")
        .replace(",","")
        .strip
      Some(auth) 
    } else {
      None
    }
    val baseURL = page.url.split("/seccion/tecnologia")(0).strip()
    val title = doc.select("h1.news__title").text.strip()
    val outBoundLinks = doc.select(".news__data a")
    .asScala
    .map(anchorTag => {
      val href = anchorTag.attr("href").strip()
      val url = if (href.startsWith("http")) {
        href
      } else {
        s"$baseURL$href"
      }
      (anchorTag.text(), url)
    })
    .filter(pair => !pair._1.isEmpty && !pair._2.isEmpty)
    .toSet

    val text = doc.select("body").text().strip()

    val dateParts =  doc.select("time.news__date")
      .attr("content")
      .strip
      .split("-") //2020-07-04
      
    val datePublished = if (dateParts.length == 3) {
      Some(ArticleDate(dateParts(2).toInt, dateParts(1).toInt, dateParts(0).toInt))
    } else {
      None
    }
  
    val webArticle = WebArticle(
      page,
      linkText,
      link,
      title,
      author,
      datePublished,
      outBoundLinks,
      None,
      text,
    )
    Some(webArticle)
    /** END MANUAL SECTION */
  }
  /** BEGIN MANUAL METHOD SECTION */
  /** END MANUAL METHOD SECTION */
}
