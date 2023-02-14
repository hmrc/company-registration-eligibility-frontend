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

package controllers.actions

import connectors.DataCacheConnector
import models.requests.{CacheIdentifierRequest, OptionalDataRequest}
import play.api.mvc.{ActionTransformer, AnyContent, BodyParser, MessagesControllerComponents}
import utils.UserAnswers

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DataRetrievalAction @Inject()(dataCacheConnector: DataCacheConnector,
                                    controllerComponents: MessagesControllerComponents) extends ActionTransformer[CacheIdentifierRequest, OptionalDataRequest] {

  override protected implicit val executionContext: ExecutionContext = controllerComponents.executionContext

  def parser: BodyParser[AnyContent] = controllerComponents.parsers.defaultBodyParser

  override protected def transform[A](request: CacheIdentifierRequest[A]): Future[OptionalDataRequest[A]] =
    dataCacheConnector.fetch(request.cacheId).map {
      case None => OptionalDataRequest(request.request, request.cacheId, None)
      case Some(data) => OptionalDataRequest(request.request, request.cacheId, Some(new UserAnswers(data)))
    }

}
