package scraper

import collection.JavaConverters._
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import common.CountryType._
import crawler.CrawlerTypes._

object WebScraperElNuevoDiaArticle extends TWebScraper {

  def apply(
    page: WebPage,
    linkText: String,
    link: URL,
    html: String,
  ): Option[WebArticle] = {
    val doc = parseHTMLDocument(html)
    val section = if (page.url.contains("tecnologia")) {
      "tecnologia"
    } else if (page.url.contains("negocios")) {
      "negocios"
    } else {
      // default to: tecnologia
      "tecnologia"
    }
    val baseURL = s"https://${getBaseDomain(page.url)}/$section"
    val title = doc.select(".title").text().strip
    val img = doc.select(".ResponsiveImage__img").attr("src").strip
    val image = if (!img.isEmpty) Some(img) else None
    val outBoundLinks = doc.select(".ENDBody a")
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

    val text = doc.select("body").text.strip
    val longDateString = if (doc.select(".item-date").text.isEmpty) {
      doc.select(".article-header").select(".toolbar-item").text
    } else {
      doc.select(".toolbar-item.item-date").text()
    }
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
  }
}