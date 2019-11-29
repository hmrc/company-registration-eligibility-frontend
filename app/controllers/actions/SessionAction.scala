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

package controllers.actions

import config.FrontendAppConfig
import controllers.routes
import javax.inject.{Inject, Singleton}
import models.requests.CacheIdentifierRequest
import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, MessagesControllerComponents, Request, Result}
import play.api.mvc.Results.Redirect
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SessionAction @Inject()(config: FrontendAppConfig,
                              controllerComponents: MessagesControllerComponents
                             ) extends ActionBuilder[CacheIdentifierRequest, AnyContent] {

  implicit override protected val executionContext: ExecutionContext = controllerComponents.executionContext

  override def parser: BodyParser[AnyContent] = controllerComponents.parsers.defaultBodyParser

  override def invokeBlock[A](request: Request[A], block: CacheIdentifierRequest[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    hc.sessionId match {
      case Some(session) =>
        block(CacheIdentifierRequest(request, session.value))
      case None =>
        Future.successful(
          Redirect(routes.SessionExpiredController.onPageLoad())
        )
    }
  }
}