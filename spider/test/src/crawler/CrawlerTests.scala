package crawler

import utest._
import scala.concurrent._, duration.Duration.Inf, ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import org.asynchttpclient.Response

object CrawlerTests extends TestSuite {

  def tests = Tests {
    test("Async download") {
      // TODO:
    }
  }

  TestRunner.runAsync(tests).map { results =>
    assert(true)
  }
}