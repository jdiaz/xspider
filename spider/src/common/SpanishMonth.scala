package common

object SpanishMonth extends Enumeration {
  type SpanishMonth = Value
  val ENERO = Value("enero")
  val FEBRERO = Value("febrero")
  val MARZO = Value("marzo")
  val ABRIL = Value("abril")
  val MAYO = Value("mayo")
  val JUNIO = Value("junio")
  val JULIO = Value("julio")
  val AGOSTO = Value("agosto")
  val SEPTIEMBRE = Value("septiembre")
  val OCTUBRE = Value("octubre")
  val NOVIEMBRE = Value("noviembre")
  val DICIEMBRE = Value("diciembre")

  def withNameOpt(s: String): Option[Value] = {
    values.find(value => value.toString == s) match {
      case Some(v) => Some(v)
      case None => abbreviatedNameOpt(s)
    }
  }

  private def abbreviatedNameOpt(s: String): Option[Value] = {
    values.find(value => value.toString.startsWith(s))
  }
}