/*
 * Copyright 2021 HM Revenue & Customs
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

import controllers.actions._
import play.api.test.Helpers._
import views.html.beforeYouStart

class BeforeYouStartControllerSpec extends ControllerSpecBase {

  val view: beforeYouStart = app.injector.instanceOf[beforeYouStart]

  object Controller extends BeforeYouStartController(
    frontendAppConfig,
    new FakeSessionAction(messagesControllerComponents),
    messagesControllerComponents,
    view
  )

  def viewAsString() = view(frontendAppConfig)(fakeRequest, messages).toString

  "BeforeYouStart Controller" must {
    "return OK and the correct view for a GET" in {
      val result = Controller.onPageLoad(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody()

      val result = Controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.PaymentOptionController.onPageLoad().url)
    }
  }

}




