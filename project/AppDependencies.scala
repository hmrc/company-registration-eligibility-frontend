import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  private val playVersion = "-play-28"

  private val logbackJsonLoggerVersion = "5.1.0"
  private val scalaTestVersion = "3.2.12"
  private val scalaTestPlusPlayVersion = "5.1.0"
  private val pegdownVersion = "1.6.0"
  private val httpCachingClientVersion = s"9.5.0$playVersion"
  private val playConditionalFormMappingVersion = s"1.9.0$playVersion"
  private val playLanguageVersion = s"5.1.0$playVersion"
  private val bootstrapVersion = "5.16.0"
  private val wireMockVersion = "2.27.2"
  private val playHmrcFrontendVersion = s"1.0.0$playVersion"
  private val hmrcMongoVersion = "0.71.0"
  private val flexmarkAllVersion = "0.62.2"

  val compile = Seq(
    ws,
    "uk.gov.hmrc.mongo" %% s"hmrc-mongo$playVersion" % hmrcMongoVersion,
    "uk.gov.hmrc" %% "logback-json-logger" % logbackJsonLoggerVersion,
    "uk.gov.hmrc" %% "http-caching-client" % httpCachingClientVersion,
    "uk.gov.hmrc" %% "play-conditional-form-mapping" % playConditionalFormMappingVersion,
    "uk.gov.hmrc" %% s"bootstrap-frontend$playVersion" % bootstrapVersion,
    "uk.gov.hmrc" %% "play-language" % playLanguageVersion,
    "uk.gov.hmrc" %% "play-frontend-hmrc" % playHmrcFrontendVersion
  )

  private def testDeps(scope: String) = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
    "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % scope,
    "org.pegdown" % "pegdown" % pegdownVersion % scope,
    "org.jsoup" % "jsoup" % "1.10.3" % scope,
    "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
    "org.scalatestplus" %% "scalacheck-1-16" % s"$scalaTestVersion.0" % scope,
    "com.vladsch.flexmark" % "flexmark-all" % flexmarkAllVersion % scope
  )

  object Test {
    def apply(): Seq[ModuleID] = testDeps("test") ++ Seq(
      "org.scalatestplus" %% "mockito-4-5" % s"$scalaTestVersion.0" % "test"
    )

  }

  object IntegrationTest {
    def apply(): Seq[ModuleID] = testDeps("it") ++ Seq(
      "com.github.tomakehurst" % "wiremock-jre8" % wireMockVersion % "it",
      "uk.gov.hmrc.mongo" %% s"hmrc-mongo-test$playVersion" % hmrcMongoVersion % "it"
    )
  }

  def apply(): Seq[ModuleID] = compile ++ Test() ++ IntegrationTest()
}
