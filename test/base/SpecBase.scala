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

package base

import config.FrontendAppConfig
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.mvc._
import play.api.test.FakeRequest
import repositories.SessionRepository
import utils.CascadeUpsert

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite {

  lazy val injector: Injector = app.injector

  lazy implicit val frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  def messagesApi: MessagesApi = messagesControllerComponents.messagesApi

  def fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

  def messages: Messages = messagesApi.preferred(fakeRequest)

  def messagesControllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]

  lazy val cascadeUpsert: CascadeUpsert = injector.instanceOf[CascadeUpsert]

  lazy val sessionRepository: SessionRepository = injector.instanceOf[SessionRepository]
}
