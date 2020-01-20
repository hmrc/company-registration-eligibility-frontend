/*
 * Copyright 2020 HM Revenue & Customs
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
import identifiers.SecureRegisterId
import play.api.Application
import play.api.http.HeaderNames
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.crypto.DefaultCookieSigner

class SecureRegisterISpec extends IntegrationSpecBase with SessionHelper with AuthHelper {

  override implicit lazy val app: Application = new GuiceApplicationBuilder().configure(fakeConfig()).build()

  override val cookieSigner: DefaultCookieSigner = app.injector.instanceOf[DefaultCookieSigner]

  private def client(path: String) = ws.url(s"http://localhost:$port/eligibility-for-setting-up-company$path").withFollowRedirects(false)

  s"GET ${routes.SecureRegisterController.onPageLoad().url}" should {
    "redirect if you have no saved data" in {
      val fResponse = client("/secure-register-form").get()
      val response = await(fResponse)

      response.status mustBe 303
    }
    "open the page if data is already stored" in {
      cacheSessionData(sessionId, SecureRegisterId.toString, true)

      val fResponse = client("/secure-register-form").withHeaders(HeaderNames.COOKIE -> getSessionCookie()).get()
      val response = await(fResponse)

      response.status mustBe 200
    }
  }
}
