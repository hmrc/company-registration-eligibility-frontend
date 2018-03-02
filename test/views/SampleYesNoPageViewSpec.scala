package views

import play.api.data.Form
import controllers.routes
import forms.SampleYesNoPageFormProvider
import views.behaviours.YesNoViewBehaviours
import models.NormalMode
import views.html.sampleYesNoPage

class SampleYesNoPageViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "sampleYesNoPage"

  val form = new SampleYesNoPageFormProvider()()

  def createView = () => sampleYesNoPage(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => sampleYesNoPage(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "SampleYesNoPage view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.SampleYesNoPageController.onSubmit(NormalMode).url)
  }
}
