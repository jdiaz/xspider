package common

trait TestUtils {
  protected def checkOptionalMatch[T](
    value: Option[T],
    expected: T,
  ): Boolean = value match {
    case Some(v) => v == expected
    case _ => false
  }
}