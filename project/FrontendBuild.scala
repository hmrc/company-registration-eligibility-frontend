import sbt._

object FrontendBuild extends Build with MicroService {

  val appName = "company-registration-eligibility-frontend"

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {
  import play.sbt.PlayImport._
  import play.core.PlayVersion

  private val logbackJsonLoggerVersion = "4.8.0"
  private val govukTemplateVersion = "5.55.0-play-26"
  private val playUiVersion = "8.11.0-play-26"
  private val hmrcTestVersion = "3.9.0-play-26"
  private val scalaTestVersion = "3.0.8"
  private val scalaTestPlusPlayVersion = "3.1.2"
  private val pegdownVersion = "1.6.0"
  private val mockitoAllVersion = "2.0.2-beta"
  private val httpCachingClientVersion = "9.0.0-play-26"
  private val simpleReactivemongoVersion = "7.30.0-play-26"
  private val playConditionalFormMappingVersion = "1.2.0-play-26"
  private val playLanguageVersion = "4.3.0-play-26"
  private val bootstrapVersion = "1.14.0"
  private val wireMockVersion = "2.25.1"
  private val reactivemongoTestVersion = "4.21.0-play-26"
  private val scalacheckVersion = "1.14.2"

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "simple-reactivemongo" % simpleReactivemongoVersion,
    "uk.gov.hmrc" %% "logback-json-logger" % logbackJsonLoggerVersion,
    "uk.gov.hmrc" %% "govuk-template" % govukTemplateVersion,
    "uk.gov.hmrc" %% "play-ui" % playUiVersion,
    "uk.gov.hmrc" %% "http-caching-client" % httpCachingClientVersion,
    "uk.gov.hmrc" %% "play-conditional-form-mapping" % playConditionalFormMappingVersion,
    "uk.gov.hmrc" %% "bootstrap-play-26" % bootstrapVersion,
    "uk.gov.hmrc" %% "play-language" % playLanguageVersion
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test : Seq[ModuleID] = ???
  }

  def testDeps(scope: String) = Seq(
    "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
    "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
    "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % scope,
    "org.pegdown" % "pegdown" % pegdownVersion % scope,
    "org.jsoup" % "jsoup" % "1.10.3" % scope,
    "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
    "org.mockito" % "mockito-all" % mockitoAllVersion % scope,
    "org.scalacheck" %% "scalacheck" % scalacheckVersion % scope
  )

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = testDeps(scope)
    }.test
  }

  object IntegrationTest {
    def apply() = new TestDependencies {
      override lazy val test = testDeps("it")
    }.test ++ Seq(
      "com.github.tomakehurst" % "wiremock-jre8" % wireMockVersion % "it",
      "uk.gov.hmrc"           %% "reactivemongo-test" % reactivemongoTestVersion % "it",
      "uk.gov.hmrc"           %% "hmrctest" % hmrcTestVersion % "it",
      "org.scalatestplus.play"  %% "scalatestplus-play" % scalaTestPlusPlayVersion % "it"
    )
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}
