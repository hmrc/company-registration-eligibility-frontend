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

package views

import views.behaviours.ViewBehaviours
import views.html.eligible

class EligibleViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "eligible"
  val redirectionUrl="http://localhost:8571/government-gateway-registration-frontend?accountType=organisation&continue=http%3A%2F%2Flocalhost%3A9970%2Fregister-your-company%2Fpost-sign-in&origin=company-registration-frontend"
  def createView = () => eligible(frontendAppConfig,redirectionUrl)(fakeRequest, messages)

  "Eligible view" must {
    behave like normalPage(createView, messageKeyPrefix)
  }
}
