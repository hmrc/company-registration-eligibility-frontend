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

package controllers

import config.FrontendAppConfig
import controllers.actions._
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.{ineligible, ineligible_payment}

@Singleton
class IneligibleController @Inject()(appConfig: FrontendAppConfig,
                                     controllerComponents: MessagesControllerComponents,
                                     identify: SessionAction) extends FrontendController(controllerComponents) with I18nSupport {

  def onPageLoad(ineligibleType: String): Action[AnyContent] = identify {
    implicit request =>
      Ok(ineligible(appConfig, ineligibleType))
  }

  def onPageLoadPayment(): Action[AnyContent] = identify {
    implicit request =>
      Ok(ineligible_payment(appConfig))
  }

  def onSubmit: Action[AnyContent] = Action { implicit request =>
    Redirect(appConfig.webincsUrl)
  }
}
