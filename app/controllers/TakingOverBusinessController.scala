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

package controllers

import config.FrontendAppConfig
import config.featureswitch.{FeatureSwitching, TakeOversAllowed}
import connectors.DataCacheConnector
import controllers.actions._
import forms.TakingOverBusinessFormProvider
import identifiers.TakingOverBusinessId
import models.NormalMode
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import utils.{Navigator, UserAnswers}
import views.html.takingOverBusiness

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TakingOverBusinessController @Inject()(appConfig: FrontendAppConfig,
                                             dataCacheConnector: DataCacheConnector,
                                             navigator: Navigator,
                                             identify: SessionAction,
                                             getData: DataRetrievalAction,
                                             requireData: DataRequiredAction,
                                             formProvider: TakingOverBusinessFormProvider,
                                             controllerComponents: MessagesControllerComponents,
                                             view: takingOverBusiness
                                            )(implicit executionContext: ExecutionContext) extends FrontendController(controllerComponents) with I18nSupport with FeatureSwitching {
  val form: Form[Boolean] = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request => {
      if (isEnabled(TakeOversAllowed)) {
        Redirect(routes.SecureRegisterController.onPageLoad())
      } else {
        val preparedForm = request.userAnswers.takingOverBusiness match {
          case None => form
          case Some(value) => form.fill(value)
        }
        Ok(view(appConfig, preparedForm, NormalMode))
      }
    }
  }


  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(view(appConfig, formWithErrors, NormalMode))),
        value =>
          dataCacheConnector.save[Boolean](request.internalId, TakingOverBusinessId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(TakingOverBusinessId, NormalMode)(new UserAnswers(cacheMap))))
      )
  }
}
