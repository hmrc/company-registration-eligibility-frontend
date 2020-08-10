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

package controllers.test

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import config.featureswitch.FeatureSwitch
import controllers.ControllerSpecBase
import org.jsoup.Jsoup
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._


class FeatureSwitchControllerSpec extends ControllerSpecBase {
  implicit val system = ActorSystem("test")

  implicit def mat: Materializer = ActorMaterializer()

  object Controller extends FeatureSwitchController(
    frontendAppConfig,
    messagesControllerComponents
  )


  "handOffFeatureSwitch" should {

    "return OK and the correct view for a GET" in {

      val fs = Map(FeatureSwitch("feature-switch.takeovers-allowed") -> false)

      val result = Controller.show(fakeRequest.withCSRFToken)
      val page = Jsoup.parse(contentAsString(result))
      status(result) mustBe OK
      page.title() must include("Feature switch")
      page.getElementById("feature-switch.takeovers-allowed").attr("value") mustBe "true"
    }
  }
}
