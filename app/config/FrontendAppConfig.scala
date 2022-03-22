/*
 * Copyright 2022 HM Revenue & Customs
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
import play.api.i18n.Lang
import play.api.mvc.Call
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.{Inject, Singleton}

@Singleton
class FrontendAppConfig @Inject()(config: ServicesConfig) {

  private def loadConfig(key: String) = config.getString(key)

  private lazy val contactHost: String = config.getString("contact-frontend.host")
  private val contactFormServiceIdentifier: String = "companyregistrationeligibilityfrontend"

  lazy val loginUrl: String = loadConfig("urls.login")
  lazy val loginContinueUrl: String = loadConfig("urls.loginContinue")

  private val configRoot: String = "microservice.services"

  lazy val compRegFEURL: String = loadConfig(s"$configRoot.company-registration-frontend.url")
  lazy val compRegFEURI: String = loadConfig(s"$configRoot.company-registration-frontend.uri")
  lazy val postSignInUrl: String = loadConfig(s"$configRoot.company-registration-frontend.postSignInUrl")

  lazy val webincsUrl: String = config.getConfString("coho-service.web-incs", throw new Exception("Couldn't get webincs URL"))

  lazy val ggMakeAccountUrl: String = loadConfig(s"$configRoot.gg-reg-fe.url")

  lazy val languageTranslationEnabled: Boolean = config.getConfBool("microservice.services.features.welsh-translation", defBool = false)

  lazy val commonFooterUrl: String = "https://www.tax.service.gov.uk/register-your-company/cookies-privacy-terms"

  lazy val helpFooterUrl: String = "https://www.gov.uk/help"

  def accessibilityStatementRoute(pageUri: String): String = s"$compRegFEURL$compRegFEURI/accessibility-statement?pageUri=$pageUri"

  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")
  )

  def routeToSwitchLanguage: String => Call = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)
}
