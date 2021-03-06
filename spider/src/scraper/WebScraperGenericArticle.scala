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

object WebScraperGenericArticle extends TWebScraper {

  def apply(
    page: WebPage,
    linkText: String,
    link: URL,
    html: String,
  ): Option[WebArticle] = {
    val doc = parseHTMLDocument(html).body()
    /** BEGIN MANUAL SECTION */
    val domain = getBaseDomain(page.url)

    val outBoundLinks = doc.select("a[href]")
    .asScala
    .map(anchorTag => {
      val href = anchorTag.attr("href").strip
      val url = if (href.startsWith("http")) {
        href
      } else {
        s"https://$domain/$href"
      }
      (anchorTag.text(), url)
    })
    .filter(pair => !pair._1.isEmpty && !pair._2.isEmpty)
    .toSet

    val author = ""
    val img = ""
    val text = doc.text()

    val wa = WebArticle(
      origin = page,
      originLinkText = linkText,
      fullyQualifiedLink = link,
      title = linkText,
      author = if (!author.isEmpty) Some(author) else None,
      datePublished = None,
      outBoundLinks = outBoundLinks,
      image = if (!img.isEmpty) Some(img) else None,
      text = text,
    )
    Some(wa)
    /** END MANUAL SECTION */
  }
  /** BEGIN MANUAL METHOD SECTION */
  /** END MANUAL METHOD SECTION */
}

