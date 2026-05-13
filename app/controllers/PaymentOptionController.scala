/*
 * Copyright 2026 HM Revenue & Customs
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
import forms.PaymentOptionFormProvider
import identifiers.PaymentOptionId
import models.NormalMode
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import service.SessionDataCacheService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import utils.{Navigator, UserAnswers}
import views.html.paymentOption

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentOptionController @Inject()(
                                         identify: SessionAction,
                                         getData: DataRetrievalAction,
                                         sessionDataCacheService: SessionDataCacheService,
                                         navigator: Navigator,
                                         formProvider: PaymentOptionFormProvider,
                                         controllerComponents: MessagesControllerComponents,
                                         view: paymentOption
                                       )(implicit executionContext: ExecutionContext, appConfig: FrontendAppConfig)
  extends FrontendController(controllerComponents) with I18nSupport {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen getData) { implicit request =>
    val preparedForm = request.userAnswers.flatMap(_.paymentOption).fold(form)(form.fill)
    Ok(view(preparedForm, NormalMode))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData).async { implicit request =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => Future.successful(BadRequest(view(formWithErrors, NormalMode))),
        value => {
          val redirectToNextPage: UserAnswers => Call = navigator.nextPage(PaymentOptionId, NormalMode)
          sessionDataCacheService
            .setPaymentOptionAndRedirectToNextPage(value)(redirectToNextPage)
        }
      )
  }
}