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

import controllers.actions.FakeSessionAction
import play.api.test.Helpers._
import views.html.needVerifiedIdentity

class NeedVerifiedIdentityControllerSpec extends ControllerSpecBase {

  val view: needVerifiedIdentity = app.injector.instanceOf[needVerifiedIdentity]

  object Controller extends NeedVerifiedIdentityController(new FakeSessionAction(messagesControllerComponents),
    messagesControllerComponents, view)

  "NeedVerifiedIdentity Controller" must {
    "return 200 for a GET" in {
      val result = Controller.onPageLoad()(fakeRequest())
      status(result) mustBe OK
    }

    "return the correct view for a GET" in {
      val result = Controller.onPageLoad()(fakeRequest())
      contentAsString(result) mustBe view()(fakeRequest(), messages, frontendAppConfig).toString
    }
  }
}
