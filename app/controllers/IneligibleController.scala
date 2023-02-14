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
import views.html.{ineligible, ineligible_payment}

import javax.inject.{Inject, Singleton}

@Singleton
class IneligibleController @Inject()(controllerComponents: MessagesControllerComponents,
                                     identify: SessionAction,
                                     ineligibleView: ineligible,
                                     ineligiblePaymentView: ineligible_payment
                                    )(implicit appConfig: FrontendAppConfig) extends FrontendController(controllerComponents) with I18nSupport {

  def onPageLoad(ineligibleType: String): Action[AnyContent] = identify {
    implicit request =>
      Ok(ineligibleView(ineligibleType))
  }

  def onPageLoadPayment(): Action[AnyContent] = identify {
    implicit request =>
      Ok(ineligiblePaymentView())
  }

  def onSubmit: Action[AnyContent] = Action {
    Redirect(appConfig.webincsUrl)
  }
}
