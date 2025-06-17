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

import forms.IdentityVerificationFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.Html
import views.behaviours.YesNoViewBehaviours
import views.html.identityVerification

class IdentityVerificationViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix: String = "identityVerification"

  val form: Form[Boolean] = new IdentityVerificationFormProvider()()
  val view: identityVerification = app.injector.instanceOf[identityVerification]

  def createView: () => Html = () => view(form, NormalMode)(fakeRequest(), messages, frontendAppConfig)

  def createViewUsingForm: Form[_] => Html = (form: Form[_]) => view(form, NormalMode)(fakeRequest(), messages, frontendAppConfig)

  "identityVerification view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix)
  }

  "idv view external gov link" in {
    frontendAppConfig.getExternalUrl("identityVerification.companyHouse") mustBe
      "https://www.gov.uk/guidance/verify-your-identity-for-companies-house"

    frontendAppConfig.getExternalUrl("identityVerification.companyHouse",
      Some("cy")) mustBe "https://www.gov.uk/guidance/verify-your-identity-for-companies-house.cy"

    frontendAppConfig.getExternalUrl("identityVerification.companyHouse",
      Some("en")) mustBe "https://www.gov.uk/guidance/verify-your-identity-for-companies-house"
  }
}
