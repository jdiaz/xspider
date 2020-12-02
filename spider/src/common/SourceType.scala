package common

object SourceType extends Enumeration {
  type SourceType = Value
  val CNN = Value("CNN")
  val EL_NUEVO_DIA = Value("El Nuevo Día")
  val EL_PAIS = Value("El País")
  val EL_TIEMPO = Value("El Tiempo")
  val PR_SCIENCE_TRUST = Value("PR Science Trust")
  val BUSINESS_INSIDER = Value("Business Insider")
  val TEST = Value("test")
}