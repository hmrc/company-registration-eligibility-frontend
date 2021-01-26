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
import views.html.eligible
import scala.concurrent.ExecutionContext.Implicits.global

class EligibleControllerSpec extends ControllerSpecBase {

  def onwardRoute = routes.EligibleController.onPageLoad()

  object Controller extends EligibleController(
    frontendAppConfig,
    messagesControllerComponents,
    new FakeSessionAction(frontendAppConfig, messagesControllerComponents)
  )
  val redirectionUrl="http://localhost:8571/government-gateway-registration-frontend?accountType=organisation&continue=http%3A%2F%2Flocalhost%3A9970%2Fregister-your-company%2Fpost-sign-in&origin=company-registration-frontend"
  def viewAsString() = eligible(frontendAppConfig,redirectionUrl)(fakeRequest, messages).toString

  "Eligible Controller" must {

    "return OK and the correct view for a GET" in {
      val result = Controller.onPageLoad(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to GG create account URL when the user hits submit" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody()

      val result = Controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include(s"${frontendAppConfig.ggMakeAccountUrl}/government-gateway-registration-frontend")
      redirectLocation(result).get must include("continue=http%3A%2F%2Flocalhost%3A9970%2Fregister-your-company%2Fpost-sign-in")
    }

  }
}




