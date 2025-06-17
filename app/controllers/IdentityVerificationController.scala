/*
 * Copyright 2025 HM Revenue & Customs
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
import connectors.DataCacheConnector
import controllers.actions._
import forms.IdentityVerificationFormProvider
import identifiers.IdentityVerificationId
import models.{IdentityVerificationAudit, NormalMode}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import utils.{Navigator, UserAnswers}
import views.html.identityVerification

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class IdentityVerificationController @Inject()(dataCacheConnector: DataCacheConnector,
                                               auditConnector: AuditConnector,
                                               navigator: Navigator,
                                               identify: SessionAction,
                                               getData: DataRetrievalAction,
                                               formProvider: IdentityVerificationFormProvider,
                                               controllerComponents: MessagesControllerComponents,
                                               view: identityVerification
                                       )(implicit executionContext: ExecutionContext, appConfig: FrontendAppConfig)
  extends FrontendController(controllerComponents) with I18nSupport {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen getData) {
    implicit request =>
      val preparedForm = request.userAnswers flatMap (_.identityVerification) match {
        case None => form
        case Some(value) => form.fill(value)
      }
      Ok(view(preparedForm, NormalMode))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, NormalMode))),
        value => {
          auditConnector.sendExplicitAudit("SCRSIdentityVerification", auditEvent(value))
          dataCacheConnector.save[Boolean](request.internalId, IdentityVerificationId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(IdentityVerificationId, NormalMode)(new UserAnswers(cacheMap))))
        }
      )
  }

  private def auditEvent(yesOrNo: Boolean): IdentityVerificationAudit = {
    val userAnswer = if (yesOrNo) "Yes"  else "No"
    IdentityVerificationAudit(haveCompanyHousePersonalCodes =  Some(userAnswer))
  }
}
