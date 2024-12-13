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

package views

import play.twirl.api.Html
import views.behaviours.ViewBehaviours
import views.html.eligible

class EligibleViewSpec extends ViewBehaviours {
  val view: eligible = app.injector.instanceOf[eligible]


  val messageKeyPrefix: String = "eligible"
  val redirectionUrl: String = "http://localhost:9553/bas-gateway/register" +
    "?accountType=organisation&continueUrl=http%3A%2F%2Flocalhost%3A9970%2Fregister-your-company%2Fpost-sign-in&origin=company-registration-frontend"

  def createView: () => Html = () => view(redirectionUrl)(fakeRequest(), messages, frontendAppConfig)

  "Eligible view" must {
    behave like normalPage(createView, messageKeyPrefix)
  }
}
