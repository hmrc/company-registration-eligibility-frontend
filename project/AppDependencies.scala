import play.core.PlayVersion
import play.sbt.PlayImport.*
import sbt.*

object AppDependencies {

  private val playVersion = "-play-30"

  private val logbackJsonLoggerVersion          = "5.5.0"
  private val scalaTestVersion                  = "3.2.19"
  private val scalaTestPlusPlayVersion          = "7.0.1"
  private val httpCachingClientVersion          = "11.2.0"
  private val playConditionalFormMappingVersion = "2.0.0"
  private val bootstrapVersion                  = "8.6.0"
  private val wireMockVersion                   = "3.0.1"
  private val playHmrcFrontendVersion           = "9.11.0"
  private val hmrcMongoVersion                  = "2.6.0"
  private val flexmarkAllVersion                = "0.64.8"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc.mongo" %% s"hmrc-mongo$playVersion"                    % hmrcMongoVersion,
    "uk.gov.hmrc"       %% "logback-json-logger"                        % logbackJsonLoggerVersion,
    "uk.gov.hmrc"       %% s"http-caching-client$playVersion"           % httpCachingClientVersion,
    "uk.gov.hmrc"       %% s"play-conditional-form-mapping$playVersion" % playConditionalFormMappingVersion,
    "uk.gov.hmrc"       %% s"bootstrap-frontend$playVersion"            % bootstrapVersion,
    "uk.gov.hmrc"       %% s"play-frontend-hmrc$playVersion"            % playHmrcFrontendVersion
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% s"bootstrap-test$playVersion"  % bootstrapVersion         % Test,
    "org.scalatest"          %% "scalatest"                    % scalaTestVersion         % Test,
    "org.scalatestplus.play" %% "scalatestplus-play"           % scalaTestPlusPlayVersion % Test,
    "org.jsoup"               % "jsoup"                        % "1.19.1"                 % Test,
    "org.playframework"      %% "play-test"                    % PlayVersion.current      % Test,
    "org.scalatestplus"      %% "scalacheck-1-16"              % "3.2.14.0"               % Test,
    "com.vladsch.flexmark"    % "flexmark-all"                 % flexmarkAllVersion       % Test,
    "org.scalatestplus"      %% "mockito-4-5"                  % "3.2.12.0"               % Test,
    "com.github.tomakehurst"  % "wiremock-jre8-standalone"     % wireMockVersion          % Test,
    "uk.gov.hmrc.mongo"      %% s"hmrc-mongo-test$playVersion" % hmrcMongoVersion         % Test
  )

  def apply(): Seq[ModuleID] = compile ++ test
}
