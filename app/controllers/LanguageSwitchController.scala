/*
 * Copyright 2019 HM Revenue & Customs
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

package controllers

import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.i18n.{I18nSupport, Lang}
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.language.{LanguageController, LanguageUtils}


@Singleton
class LanguageSwitchController @Inject()(configuration: Configuration,
                                         appConfig: FrontendAppConfig,
                                         controllerComponents: MessagesControllerComponents,
                                         languageUtils: LanguageUtils
                                        ) extends LanguageController(configuration, languageUtils, controllerComponents) with I18nSupport {

  override def fallbackURL: String = routes.IndexController.onPageLoad().url

  override def languageMap: Map[String, Lang] = appConfig.languageMap

}
