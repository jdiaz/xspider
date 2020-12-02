// build.sc
import mill._, scalalib._

object spider extends ScalaModule {
  def scalaVersion = "2.12.10"

  def ivyDeps = Agg(
    ivy"org.jsoup:jsoup:1.12.1",
    ivy"org.asynchttpclient:async-http-client:2.5.2",
    ivy"com.lihaoyi::upickle:0.7.1",
    ivy"ch.qos.logback:logback-classic:1.2.3",
  )

  object test extends Tests {
    def ivyDeps = Agg(
      ivy"com.lihaoyi::utest:0.7.4",
    )
    def testFrameworks = Seq("utest.runner.Framework")
  }
}