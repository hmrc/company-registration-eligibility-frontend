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

import controllers.actions._
import play.api.mvc.Call
import play.api.test.Helpers._
import views.html.{ineligible, ineligible_payment}

class IneligibleControllerSpec extends ControllerSpecBase {

  def onwardRoute: Call = routes.IndexController.onPageLoad

  val viewIneligible: ineligible = app.injector.instanceOf[ineligible]
  val viewIneligiblePayment: ineligible_payment = app.injector.instanceOf[ineligible_payment]

  def controller() = new IneligibleController(
    messagesControllerComponents,
    new FakeSessionAction(messagesControllerComponents),
    viewIneligible,
    viewIneligiblePayment
  )(frontendAppConfig)

  def viewAsString(): String = viewIneligible("foo")(fakeRequest(), messages, frontendAppConfig).toString

  "Ineligible Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad("foo")(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest("POST").withFormUrlEncodedBody()

      val result = controller().onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(frontendAppConfig.webincsUrl)
    }

  }
}




