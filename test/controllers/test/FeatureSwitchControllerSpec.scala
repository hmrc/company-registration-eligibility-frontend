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

package controllers.test

import akka.actor.ActorSystem
import config.featureswitch.FeatureSwitch.WelshEnabled
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
  }

}
