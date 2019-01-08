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
import javax.inject.Inject

import config.FrontendAppConfig
import controllers.actions._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.eligible

class EligibleControllerImpl @Inject()(val appConfig: FrontendAppConfig,
                                       override val messagesApi: MessagesApi,
                                       val identify: CacheIdentifierAction) extends EligibleController {
  val postSignInUri     = appConfig.postSignInUrl
  val ggMakeAccountUrl  = appConfig.ggMakeAccountUrl
  val companyRegURI     = s"${appConfig.compRegFEURL}${appConfig.compRegFEURI}"
}

trait EligibleController extends FrontendController with I18nSupport {
  val appConfig : FrontendAppConfig
  val companyRegURI : String
  val ggMakeAccountUrl : String
  val postSignInUri : String
  val identify : CacheIdentifierAction

  private[controllers] def buildCreateAccountURL: String = {
    val crfePostSignIn = s"$companyRegURI$postSignInUri"
    val ggrf = "government-gateway-registration-frontend"
    val accountType = "accountType=organisation"
    val origin = "origin=company-registration-frontend"
    val continueURL = s"continue=${URLEncoder.encode(s"$crfePostSignIn","UTF-8")}"
    s"$ggMakeAccountUrl/$ggrf?$accountType&$continueURL&$origin"
  }

  def onPageLoad: Action[AnyContent] = identify {
    implicit request =>
      Ok(eligible(appConfig))
  }

  def onSubmit: Action[AnyContent] = Action { implicit request =>
    Redirect(buildCreateAccountURL)
  }
}
