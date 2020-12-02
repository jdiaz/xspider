package crawler

import java.io.File
import java.io.PrintWriter
import java.util.Date

import CrawlerTypes._
import common.CountryType._
import common.CountryType
import common.SourceType._
import common.SourceType

object CrawlerUtils {

  val endiTech = "https://www.elnuevodia.com/tecnologia/"
  val endiBusiness = "https://www.elnuevodia.com/negocios/"
  
  val pais = "https://www.elpais.com/tecnologia/"
  val paisEconomy = "https://elpais.com/economia/"
  val paisAmericasEconomy = "https://elpais.com/america/economia/"

  val tiempo = "https://eltiempo.com/tecnosfera/"
  val tiempoEconomia = "https://eltiempo.com/economia"
  val tiempoEmpresas = "https://eltiempo.com/economia/empresas"
  
  val businessInsider = "https://www.businessinsider.es/tecnologia"
  val businessInsiderEconomia = "https://www.businessinsider.es/economia"
  
  val cnn = "https://cnnespanol.cnn.com/seccion/tecnologia/"
  val prScinceTrust = "https://prsciencetrust.org/blog/"

  def loadWebPageSourcesFromMemory(): Set[WebPage] = Set(
    WebPage(endiTech, CountryType.PUERTORICO, SourceType.EL_NUEVO_DIA),
    WebPage(endiBusiness, CountryType.PUERTORICO, SourceType.EL_NUEVO_DIA),
    WebPage(pais, CountryType.ESPANA, SourceType.EL_PAIS),
    WebPage(paisEconomy, CountryType.ESPANA, SourceType.EL_PAIS),
    WebPage(paisAmericasEconomy, CountryType.ESPANA, SourceType.EL_PAIS),
    WebPage(businessInsiderEconomia, CountryType.ESPANA, SourceType.BUSINESS_INSIDER),
    WebPage(businessInsider, CountryType.ESPANA, SourceType.BUSINESS_INSIDER),
    WebPage(tiempo, CountryType.COLOMBIA, SourceType.EL_TIEMPO),
    WebPage(tiempoEconomia, CountryType.COLOMBIA, SourceType.EL_TIEMPO),
    WebPage(tiempoEmpresas, CountryType.COLOMBIA, SourceType.EL_TIEMPO),
    WebPage(cnn, CountryType.EEUU, SourceType.CNN),
    WebPage(prScinceTrust, CountryType.PUERTORICO, SourceType.PR_SCIENCE_TRUST),
  )

  def webArticlesToJSON(
    webArticles: Traversable[WebArticle],
    identation: Int = 4,
  ): String = {
    val jsonArr = webArticles.map { wa => 
      ujson.Obj(
        "title" -> ujson.Str(wa.title),
        "originLinkText" -> ujson.Str(wa.originLinkText),
        "source" -> ujson.Str(wa.origin.source.toString),
        "sourceURL" -> ujson.Str(wa.origin.url),
        "country" -> ujson.Str(wa.origin.country.toString),
        "author" -> ujson.Str(wa.author.getOrElse("")),
        "date" -> ujson.Str(wa.datePublished match {
          case Some(date: ArticleDate) => s"${date.day}/${date.month}/${date.year}"
          case _ => ""
        }),
        "url" -> ujson.Str(wa.fullyQualifiedLink),
        "textLength" -> ujson.Num(wa.text.length),        
        "image" -> ujson.Str(wa.image.getOrElse("")),
        "outBoundLinksCount" -> ujson.Num(wa.outBoundLinks.size),
        "outBoundLinks" -> ujson.Arr(
          wa.outBoundLinks.map(linkPair => ujson.Obj(
            "linkText" -> linkPair._1,
            "url" -> linkPair._2,
          ))
        ),
        "text" -> ujson.Str(wa.text),
      )
    }

    ujson.write(jsonArr, indent = identation)
  }

  def writeTextToFile(
    fileName: String,
    text: String,
  ): Boolean =
    if (text.isEmpty) false
    else {
      val unixTime = (new Date()).getTime()
      val writer = new PrintWriter(new File(s"${fileName}_${unixTime}.json"))
      writer.write(text)
      writer.close()
      true
    }

  def getNameFromURL(
    url: URL,
  ): String = {
    val parts = url.split("//")
    if (parts.length < 2) url 
    else parts(1).split("/")(0).stripPrefix("www.")
  }
}
