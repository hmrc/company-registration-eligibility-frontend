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

package views

import forms.SecureRegisterFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.Html
import views.behaviours.YesNoViewBehaviours
import views.html.secureRegister

class SecureRegisterViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix: String = "secureRegister"
  val view: secureRegister = app.injector.instanceOf[secureRegister]
  val form: Form[Boolean] = new SecureRegisterFormProvider()()

  def createView: () => Html = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm: Form[_] => Html = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  "SecureRegister view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix)
  }
}
