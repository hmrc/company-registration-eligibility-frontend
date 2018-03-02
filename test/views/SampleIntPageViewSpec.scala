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
import forms.SampleIntPageFormProvider
import models.NormalMode
import views.behaviours.IntViewBehaviours
import views.html.sampleIntPage

class SampleIntPageViewSpec extends IntViewBehaviours {

  val messageKeyPrefix = "sampleIntPage"

  val form = new SampleIntPageFormProvider()()

  def createView = () => sampleIntPage(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => sampleIntPage(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "SampleIntPage view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like intPage(createViewUsingForm, messageKeyPrefix, routes.SampleIntPageController.onSubmit(NormalMode).url)
  }
}
