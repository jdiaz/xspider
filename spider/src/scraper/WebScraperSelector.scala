package scraper

import common.SourceType._
import common.SourceType
import crawler.CrawlerTypes._
import scraper._

object WebScraperSelector {

  def apply(
    source: SourceType,
    page: WebPage,
    linkText: String,
    linkURL: URL,
    html: String,
  ): Option[WebArticle] = source match {
    case SourceType.EL_NUEVO_DIA => 
      WebScraperElNuevoDiaArticle(page, linkText, linkURL, html)
    case SourceType.EL_TIEMPO =>
      WebScraperElTiempoArticle(page, linkText, linkURL, html)
    case SourceType.EL_PAIS =>
      WebScraperElPaisArticle(page, linkText, linkURL, html)
    case SourceType.CNN =>
      WebScraperCNNArticle(page, linkText, linkURL, html)
    case SourceType.PR_SCIENCE_TRUST =>
      WebScraperPRScienceTrustArticle(page, linkText, linkURL, html)
    case SourceType.BUSINESS_INSIDER =>
      WebScraperBusinessInsiderArticle(page, linkText, linkURL, html)
    case _ => WebScraperGenericArticle(page, linkText, linkURL, html)
  }

}