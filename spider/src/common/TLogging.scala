package common

import org.slf4j.LoggerFactory

trait TLogging {
  val log = LoggerFactory.getLogger(getClass.getSimpleName())
}