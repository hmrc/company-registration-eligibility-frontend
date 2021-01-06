/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package config

import controllers.routes
import javax.inject.{Inject, Singleton}
import play.api.i18n.Lang
import play.api.mvc.Call
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class FrontendAppConfig @Inject()(config: ServicesConfig) {

  private def loadConfig(key: String) = config.getString(key)

  private lazy val contactHost = config.getString("contact-frontend.host")
  private val contactFormServiceIdentifier = "companyregistrationeligibilityfrontend"

  lazy val analyticsToken = loadConfig(s"google-analytics.token")
  lazy val analyticsHost = loadConfig(s"google-analytics.host")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  lazy val loginUrl = loadConfig("urls.login")
  lazy val loginContinueUrl = loadConfig("urls.loginContinue")

  private val configRoot = "microservice.services"

  lazy val compRegFEURL = loadConfig(s"$configRoot.company-registration-frontend.url")
  lazy val compRegFEURI = loadConfig(s"$configRoot.company-registration-frontend.uri")
  lazy val postSignInUrl = loadConfig(s"$configRoot.company-registration-frontend.postSignInUrl")
  lazy val feedbackUrl = loadConfig(s"$configRoot.company-registration-frontend.feedbackUrl")

  lazy val webincsUrl = config.getConfString("coho-service.web-incs", throw new Exception("Couldn't get webincs URL"))

  lazy val ggMakeAccountUrl = loadConfig(s"$configRoot.gg-reg-fe.url")

  lazy val languageTranslationEnabled = config.getConfBool("microservice.services.features.welsh-translation", defBool = false)

  lazy val commonFooterUrl = "https://www.tax.service.gov.uk/register-your-company/cookies-privacy-terms"

  lazy val helpFooterUrl = "https://www.gov.uk/help"

  def accessibilityStatementRoute(pageUri: String) = s"$compRegFEURL$compRegFEURI/accessibility-statement?pageUri=$pageUri"

  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")
  )

  def routeToSwitchLanguage: String => Call = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)
}
