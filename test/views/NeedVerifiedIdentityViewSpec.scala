/*
 * Copyright 2025 HM Revenue & Customs
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
import views.html.needVerifiedIdentity

class NeedVerifiedIdentityViewSpec extends ViewBehaviours {
  val view: needVerifiedIdentity = app.injector.instanceOf[needVerifiedIdentity]
  def createView: () => Html = () => view()(fakeRequest(), messages, frontendAppConfig)

  "Need VerifiedIdentity view" must {

    behave like normalPage(createView, "needVerifiedIdentity")
  }
}
