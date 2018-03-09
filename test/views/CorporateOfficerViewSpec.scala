/*
 * Copyright 2018 HM Revenue & Customs
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

import play.api.data.Form
import controllers.routes
import forms.CorporateOfficerFormProvider
import views.behaviours.YesNoViewBehaviours
import models.NormalMode
import views.html.corporateOfficer

class CorporateOfficerViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "corporateOfficer"

  val form = new CorporateOfficerFormProvider()()

  def createView = () => corporateOfficer(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => corporateOfficer(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "CorporateOfficer view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.CorporateOfficerController.onSubmit().url)
  }
}
