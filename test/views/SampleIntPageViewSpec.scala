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
