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
import scala.concurrent.ExecutionContext.Implicits.global


class FeedbackControllerSpec extends ControllerSpecBase {

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap): FeedbackController =
    new FeedbackController(frontendAppConfig, messagesControllerComponents) {
      override val feedbackUrl     = "feUr"
      override val frontendUrl     = "frUr"
    }

  "Eligible Controller" must {

    "redirect to company registration feedback submission on show" in {
      val req = fakeRequest.withFormUrlEncodedBody()

      val result = controller().show()(req)

      status(result) mustBe SEE_OTHER
      redirectLocation(result).get must include("frUrfeUr")
    }

  }
}




