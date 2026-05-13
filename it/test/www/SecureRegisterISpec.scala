/*
 * Copyright 2026 HM Revenue & Customs
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

package test.www

import controllers.routes
import identifiers.SecureRegisterId
import play.api.Application
import play.api.http.HeaderNames
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.crypto.DefaultCookieSigner
import play.api.libs.ws.WSRequest
import test.helpers.{AuthHelper, IntegrationSpecBase, SessionHelper}

import java.util.UUID

class SecureRegisterISpec extends IntegrationSpecBase with SessionHelper with AuthHelper {

  override implicit lazy val app: Application = new GuiceApplicationBuilder().disable[play.filters.csrf.CSRFFilter]
    .configure(fakeConfig()).build()
  override val cookieSigner: DefaultCookieSigner = app.injector.instanceOf[DefaultCookieSigner]
  val csrfToken: String = UUID.randomUUID().toString
  override val sessionCookie: String = getSessionCookie(Map("csrfToken" -> csrfToken, "sessionId" -> "test-session-id"))

  def authenticated: WSRequest = {
    stubAudits()
    client.withHttpHeaders(HeaderNames.COOKIE -> sessionCookie)
  }

  private def client: WSRequest =
    ws.url(s"http://localhost:$port/eligibility-for-setting-up-company/secure-register-form").withFollowRedirects(false)

  s"GET ${routes.SecureRegisterController.onPageLoad().url}" should {
    "redirect if you have no saved data" in {
      val fResponse = client.get()
      val response = await(fResponse)

      response.status mustBe 303
    }

    "open the page if data is already stored" in {
      cacheSessionData(SecureRegisterId.toString, data = true)

      val fResponse = authenticated.get()
      val response = await(fResponse)

      response.status mustBe 200
    }

    "redirect to session expired when no session cookie is present" in {
      val response = await(client.get())

      response.status mustBe 303
      response.header(HeaderNames.LOCATION) mustBe Some(
        routes.SessionExpiredController.onPageLoad.url
      )
    }

    "redirect to session expired when session exists but no cached data" in {

      val response = await(
        authenticated.get()
      )

      response.status mustBe 303
      response.header(HeaderNames.LOCATION) mustBe Some(
        routes.SessionExpiredController.onPageLoad.url
      )
    }

    "pre-populate form when data exists in session" in {
      cacheSessionData(SecureRegisterId.toString, data = true)

      val response = await(
        authenticated.get()
      )

      response.status mustBe 200
      response.body must include("value=\"true\"")
    }
  }

  s"POST ${routes.SecureRegisterController.onSubmit().url}" should {

    "return FORBIDDEN when invalid data submitted" in {

      val response = await(
        authenticated
          .post(Map("value" -> Seq("invalid")))
      )

      response.status mustBe 403
    }

    "redirect to session expired on POST when no session" in {
      val response = await(
        ws.url(s"http://localhost:$port/eligibility-for-setting-up-company/secure-register-form")
          .withFollowRedirects(false)
          .post(Map("value" -> Seq("true")))
      )

      response.status mustBe 303
      response.header(HeaderNames.LOCATION) mustBe Some(
        routes.SessionExpiredController.onPageLoad.url
      )
    }
  }
}
