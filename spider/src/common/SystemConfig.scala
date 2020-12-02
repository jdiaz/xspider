package common

import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors

object SystemConfig {
  val CPU_COEFICIENT = 1
  val CORES = Runtime.getRuntime().availableProcessors() * CPU_COEFICIENT;

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    Executors.newFixedThreadPool(CORES),
  )
}