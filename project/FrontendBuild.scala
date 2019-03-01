import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object FrontendBuild extends Build with MicroService {

  val appName = "company-registration-eligibility-frontend"

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {
  import play.sbt.PlayImport._
  import play.core.PlayVersion

  private val playHealthVersion = "3.12.0-play-25"
  private val logbackJsonLoggerVersion = "3.1.0"
  private val govukTemplateVersion = "5.22.0"
  private val playUiVersion = "7.32.0-play-25"
  private val hmrcTestVersion = "3.5.0-play-25"
  private val scalaTestVersion = "3.0.4"
  private val scalaTestPlusPlayVersion = "2.0.1"
  private val pegdownVersion = "1.6.0"
  private val mockitoAllVersion = "2.0.2-beta"
  private val httpCachingClientVersion = "8.1.0"
  private val simpleReactivemongoVersion = "7.12.0-play-25"
  private val playConditionalFormMappingVersion = "0.2.0"
  private val playLanguageVersion = "3.4.0"
  private val bootstrapVersion = "4.9.0"
  private val scalacheckVersion = "1.13.4"
  private val jsoupVersion = "1.11.2"
  private val scoverageVersion = "1.3.1"
  private val wireMockVersion = "2.6.0"
  private val reactivemongoTestVersion = "4.9.0-play-25"
  private val playWhitelistVersion     = "2.0.0"
  private val frontendBootstrapVersion = "8.24.0"

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "simple-reactivemongo" % simpleReactivemongoVersion,
    "uk.gov.hmrc" %% "logback-json-logger" % logbackJsonLoggerVersion,
    "uk.gov.hmrc" %% "govuk-template" % govukTemplateVersion,
    "uk.gov.hmrc" %% "play-health" % playHealthVersion,
    "uk.gov.hmrc" %% "play-ui" % playUiVersion,
    "uk.gov.hmrc" %% "http-caching-client" % httpCachingClientVersion,
    "uk.gov.hmrc" %% "play-conditional-form-mapping" % playConditionalFormMappingVersion,
    "uk.gov.hmrc" %% "bootstrap-play-25" % bootstrapVersion,
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
      "com.github.tomakehurst" % "wiremock" % wireMockVersion % "it",
      "uk.gov.hmrc"           %% "reactivemongo-test" % reactivemongoTestVersion % "it",
      "uk.gov.hmrc"           %% "hmrctest" % hmrcTestVersion % "it",
      "org.scalatestplus.play"  %% "scalatestplus-play" % scalaTestPlusPlayVersion % "it"
    )
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}
