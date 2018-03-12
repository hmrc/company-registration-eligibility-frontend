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
import helpers.FakeConfig
import itutil.WiremockHelper
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpecLike}
import org.scalatestplus.play.OneServerPerSuite
import play.api.test.FakeApplication
import uk.gov.hmrc.play.test.UnitSpec

class TooManyDirectorsISpec extends FakeConfig with UnitSpec
  with OneServerPerSuite with WordSpecLike with WiremockHelper with BeforeAndAfterAll with BeforeAndAfterEach {
  val mockPort = 11111
  val mockHost = "localhost"
  val url = s"http://$mockHost:$mockPort"

  override implicit lazy val app = FakeApplication(additionalConfiguration = fakeConfig())

  private def client(path: String) = ws.url(s"http://localhost:$port/company-registration-eligibility$path").withFollowRedirects(false)

  override def beforeAll() = {
    super.beforeAll()
    startWiremock()
  }

  override def afterAll() = {
    stopWiremock()
    super.afterAll()
  }

  override def beforeEach() = {
    resetWiremock()
  }

  s"GET ${routes.TooManyDirectorsController.onPageLoad().url}" should {

    "return the do you want to register more than five directors page" in {
      val fResponse = client("/register-more-than-five-directors").get()
      val response = await(fResponse)

      response.status shouldBe 200
    }
  }
}
