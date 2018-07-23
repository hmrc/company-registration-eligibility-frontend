/*
 * Copyright 2017 HM Revenue & Customs
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

package www

import controllers.routes
import helpers.{AuthHelper, IntegrationSpecBase, SessionHelper}
import identifiers.PaymentOptionId
import play.api.http.HeaderNames
import play.api.test.FakeApplication

class TooManyDirectorsISpec extends IntegrationSpecBase with SessionHelper with AuthHelper {

  override implicit lazy val app = FakeApplication(additionalConfiguration = fakeConfig())

  private def client(path: String) = ws.url(s"http://localhost:$port/eligibility-for-setting-up-company$path").withFollowRedirects(false)

  s"GET ${routes.TooManyDirectorsController.onPageLoad().url}" should {
    "redirect if you have no saved data" in {
      val fResponse = client("/register-more-than-five-directors").get()
      val response = await(fResponse)

      response.status mustBe 303
    }
    "open the page if data is already stored" in {
      cacheSessionData(sessionId, PaymentOptionId.toString, true)

      val fResponse = client("/register-more-than-five-directors").withHeaders(HeaderNames.COOKIE -> getSessionCookie()).get()
      val response = await(fResponse)

      response.status mustBe 200
    }
  }
}
