/*
 * Copyright 2023 HM Revenue & Customs
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
import controllers.actions._
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.eligible

import java.net.URLEncoder
import javax.inject.{Inject, Singleton}

@Singleton
class EligibleController @Inject()(controllerComponents: MessagesControllerComponents,
                                   identify: SessionAction,
                                   view: eligible
                                  )(implicit appConfig: FrontendAppConfig) extends FrontendController(controllerComponents) with I18nSupport {

  private val redirectionUrl = {
    val companyRegURI = s"${appConfig.compRegFEURL}${appConfig.compRegFEURI}"
    val crfePostSignIn = s"$companyRegURI${appConfig.postSignInUrl}"
    val ggrf = "/bas-gateway/register"
    val accountType = "accountType=organisation"
    val origin = "origin=company-registration-frontend"
    val continueURL = s"continueUrl=${URLEncoder.encode(s"$crfePostSignIn", "UTF-8")}"

    s"${appConfig.ggMakeAccountUrl}$ggrf?$accountType&$continueURL&$origin"
  }

  def onPageLoad: Action[AnyContent] = identify {
    implicit request =>
      Ok(view(redirectionUrl))
  }

  def onSubmit: Action[AnyContent] = Action {
      Redirect(redirectionUrl)
  }

}
