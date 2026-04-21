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
import connectors.SessionDataCacheConnector
import controllers.actions._
import forms.PaymentOptionFormProvider
import identifiers.PaymentOptionId
import models.NormalMode
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionCacheId.NoSessionException
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import utils.{Navigator, UserAnswers}
import views.html.paymentOption

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentOptionController @Inject()(dataCacheConnector: SessionDataCacheConnector,
                                        navigator: Navigator,
                                        identify: SessionAction,
                                        formProvider: PaymentOptionFormProvider,
                                        controllerComponents: MessagesControllerComponents,
                                        view: paymentOption
                                       )(implicit executionContext: ExecutionContext, appConfig: FrontendAppConfig) extends FrontendController(controllerComponents) with I18nSupport {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(): Action[AnyContent] = identify.async { implicit request =>
    dataCacheConnector.fetchPaymentOptionFromSession.map { paymentOption =>
      Ok(view(paymentOption.fold(form)(form.fill), NormalMode))
    }.recover {
      case NoSessionException =>
        Redirect(routes.SessionExpiredController.onPageLoad)
    }
  }

  def onSubmit(): Action[AnyContent] = identify.async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, NormalMode))),
      value =>
        dataCacheConnector
          .savePaymentOptionToSession(value)
          .map(userAnswers =>
            Redirect(navigator.nextPage(PaymentOptionId, NormalMode)(userAnswers))
          )
    )
  }
}
