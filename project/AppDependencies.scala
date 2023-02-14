import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  private val playVersion = "-play-28"

  private val logbackJsonLoggerVersion          = "5.2.0"
  private val scalaTestVersion                  = "3.2.15"
  private val scalaTestPlusPlayVersion          = "5.1.0"
  private val httpCachingClientVersion          = s"10.0.0$playVersion"
  private val playConditionalFormMappingVersion = s"1.12.0$playVersion"
  private val bootstrapVersion                  = "7.13.0"
  private val wireMockVersion                   = "2.35.0"
  private val playHmrcFrontendVersion           = s"6.3.0$playVersion"
  private val hmrcMongoVersion                  = "0.74.0"
  private val flexmarkAllVersion                = "0.64.0"

  val compile = Seq(
    ws,
    "uk.gov.hmrc.mongo"       %% s"hmrc-mongo$playVersion"          %  hmrcMongoVersion,
    "uk.gov.hmrc"             %%  "logback-json-logger"             %  logbackJsonLoggerVersion,
    "uk.gov.hmrc"             %%  "http-caching-client"             %  httpCachingClientVersion,
    "uk.gov.hmrc"             %%  "play-conditional-form-mapping"   %  playConditionalFormMappingVersion,
    "uk.gov.hmrc"             %% s"bootstrap-frontend$playVersion"  %  bootstrapVersion,
    "uk.gov.hmrc"             %%  "play-frontend-hmrc"              %  playHmrcFrontendVersion
  )

  val test = Seq(
    "uk.gov.hmrc"             %% s"bootstrap-test$playVersion"      %  bootstrapVersion           % "test, it",
    "org.scalatest"           %%  "scalatest"                       %  scalaTestVersion           % "test, it",
    "org.scalatestplus.play"  %%  "scalatestplus-play"              %  scalaTestPlusPlayVersion   % "test, it",
    "org.jsoup"               %   "jsoup"                           %  "1.15.3"                   % "test, it",
    "com.typesafe.play"       %%  "play-test"                       %  PlayVersion.current        % "test, it",
    "org.scalatestplus"       %%  "scalacheck-1-16"                 % "3.2.14.0"                  % "test, it",
    "com.vladsch.flexmark"    %   "flexmark-all"                    %  flexmarkAllVersion         % "test, it",
    "org.scalatestplus"       %%  "mockito-4-5"                     % "3.2.12.0"                  % "test",
    "com.github.tomakehurst"  %   "wiremock-jre8-standalone"        %  wireMockVersion            % "it",
    "uk.gov.hmrc.mongo"       %% s"hmrc-mongo-test$playVersion"     %  hmrcMongoVersion           % "it"
  )

  def apply(): Seq[ModuleID] = compile ++ test
}
