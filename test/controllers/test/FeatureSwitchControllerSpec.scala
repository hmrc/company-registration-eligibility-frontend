/*
 * Copyright 2023 HM Revenue & Customs
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

package controllers.test

import org.apache.pekko.actor.ActorSystem
import config.featureswitch.FeatureSwitch.{ScrsIdvEnabled, WelshEnabled}
import config.featureswitch.FeatureSwitching
import controllers.ControllerSpecBase
import org.jsoup.Jsoup
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._
import views.html.feature_switches


class FeatureSwitchControllerSpec extends ControllerSpecBase with FeatureSwitching {
  implicit val system: ActorSystem = ActorSystem("test")

  val view: feature_switches = app.injector.instanceOf[feature_switches]
  object Controller extends FeatureSwitchController(
    frontendAppConfig,
    messagesControllerComponents,
    view
  )

  "enableWelshFeatureSwitch" should {
    "return OK and the correct view for a GET with Welsh Enabled" in {
      enable(WelshEnabled)
      val result = Controller.show(fakeRequest().withCSRFToken)
      val page = Jsoup.parse(contentAsString(result))
      status(result) mustBe OK
      page.title() must include("Feature switch")
      page.getElementById("feature-switch.enable-welsh").attr("value") mustBe "true"
    }

    "return OK and the correct view for a GET with Welsh Disabled" in {
      disable(WelshEnabled)
      val result = Controller.show(fakeRequest().withCSRFToken)
      val page = Jsoup.parse(contentAsString(result))
      status(result) mustBe OK
      page.title() must include("Feature switch")
      page.getElementById("feature-switch.enable-welsh").attr("value") mustBe "false"
    }

    "change state of switches when posting" in {

      disable(WelshEnabled)
      isEnabled(WelshEnabled) mustBe false

      val result = Controller.submit(fakeRequest("POST").withFormUrlEncodedBody(
        WelshEnabled.name -> "true"
      ).withCSRFToken)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.FeatureSwitchController.show.url)

      isEnabled(WelshEnabled) mustBe true
    }
  }

  "enable SCRS IDV feature switch" should {
    "return OK and the correct view for a GET with SCRS IDV Enabled" in {
      enable(ScrsIdvEnabled)
      val result = Controller.show(fakeRequest().withCSRFToken)
      val page = Jsoup.parse(contentAsString(result))
      status(result) mustBe OK
      page.title() must include("Feature switch")
      page.getElementById("feature-switch.scrs-idv").attr("value") mustBe "true"
    }

    "return OK and the correct view for a GET with SCRS IDV Disabled" in {
      disable(ScrsIdvEnabled)
      val result = Controller.show(fakeRequest().withCSRFToken)
      val page = Jsoup.parse(contentAsString(result))
      status(result) mustBe OK
      page.title() must include("Feature switch")
      page.getElementById("feature-switch.scrs-idv").attr("value") mustBe "false"
    }

    "change state of scrs switche when posting" in {

      disable(ScrsIdvEnabled)
      isEnabled(ScrsIdvEnabled) mustBe false

      val result = Controller.submit(fakeRequest("POST").withFormUrlEncodedBody(
        ScrsIdvEnabled.name -> "true"
      ).withCSRFToken)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.FeatureSwitchController.show.url)

      isEnabled(ScrsIdvEnabled) mustBe true
    }

  }
}
