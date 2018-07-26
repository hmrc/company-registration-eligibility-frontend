/*
 * Copyright 2018 HM Revenue & Customs
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
import helpers.MockMessages
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.mvc.{AnyContentAsEmpty, Cookie}
import play.api.test.FakeRequest

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite {

  def injector: Injector = app.injector

  def frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  implicit class FakeRequestWithLanguage[T](request: FakeRequest[T]) {
    def withLang(code: String): FakeRequest[T] = request
      .withCookies(Cookie("PLAY_LANG", code)) //only used if a fake application is running
      .withHeaders("Accept-Language" -> code) //this is the fallback
  }

  def messagesApi: MessagesApi = MockMessages()

  // to set the language you could use the cookie and set PLAY_LANG or set the header Accept-Language
  def fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("", "").withLang("en")

  def messages: Messages = messagesApi.preferred(fakeRequest)
}
