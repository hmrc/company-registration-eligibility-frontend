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

import java.net.URLEncoder

import config.FrontendAppConfig
import controllers.actions._
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.eligible

@Singleton
class EligibleController @Inject()(appConfig: FrontendAppConfig,
                                   controllerComponents: MessagesControllerComponents,
                                   identify: SessionAction
                                  ) extends FrontendController(controllerComponents) with I18nSupport {

  private val redirectionUrl = {
    val companyRegURI = s"${appConfig.compRegFEURL}${appConfig.compRegFEURI}"
    val crfePostSignIn = s"$companyRegURI${appConfig.postSignInUrl}"
    val ggrf = "government-gateway-registration-frontend"
    val accountType = "accountType=organisation"
    val origin = "origin=company-registration-frontend"
    val continueURL = s"continue=${URLEncoder.encode(s"$crfePostSignIn", "UTF-8")}"

    s"${appConfig.ggMakeAccountUrl}/$ggrf?$accountType&$continueURL&$origin"
  }

  def onPageLoad: Action[AnyContent] = identify {
    implicit request =>
      Ok(eligible(appConfig))
  }

  def onSubmit: Action[AnyContent] = Action {
    implicit request =>
      Redirect(redirectionUrl)
  }

}
