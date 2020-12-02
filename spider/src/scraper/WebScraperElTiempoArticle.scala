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

object WebScraperElTiempoArticle extends TWebScraper {

  def apply(
    page: WebPage,
    linkText: String,
    link: URL,
    html: String,
  ): Option[WebArticle] = {
    val doc = parseHTMLDocument(html)
    /** BEGIN MANUAL SECTION */
    val title = doc.select("h1.titulo").text().strip
    //val baseURL = page.url.split("/tecnosfera")(0).strip
    val section = if (page.url.contains("tecnosfera")) {
      "tecnosfera"
    } else if (page.url.contains("economia") && page.url.contains("empresas")) {
      "economia/empresas"
    } else if (page.url.contains("economia")) {
      "economia"
    } else {
      "tecnosfera"
    }
    val base = getBaseDomain(page.url)
    val baseURL = s"https://$base/$section"
    val img = doc.select(".figure-apertura-bk img").attr("src").strip
    val image = if (!img.isEmpty) Some(s"https://$base$img") else None
    val outBoundLinks = doc.select(".articulo-contenido a")
      .asScala
      .map(anchorTag => {
        val href = anchorTag.attr("href").strip
        val fullOutboundLink = if (href.startsWith("http")) {
          href
        } else {
          s"https://$baseURL$href"
        }
        (anchorTag.text().strip(), fullOutboundLink)
      })
      .filter(pair => !pair._1.isEmpty && !pair._2.isEmpty)
      .toSet
    val text = doc.select("body").text.strip
    val longDateString = doc.select(".fecha-publicacion-bk").text.strip
    val datePublished = scrapeSpanishDate(longDateString)

    val webArticle = WebArticle(
      page,
      linkText,
      link,
      title,
      None,
      datePublished,
      outBoundLinks,
      image,
      text,
    )
    Some(webArticle)
    /** END MANUAL SECTION */
  }
  /** BEGIN MANUAL METHOD SECTION */
  /** END MANUAL METHOD SECTION */
}
