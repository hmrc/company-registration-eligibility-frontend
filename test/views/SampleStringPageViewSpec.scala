package views

import play.api.data.Form
import controllers.routes
import forms.SampleStringPageFormProvider
import models.NormalMode
import views.behaviours.StringViewBehaviours
import views.html.sampleStringPage

class SampleStringPageViewSpec extends StringViewBehaviours {

  val messageKeyPrefix = "sampleStringPage"

  val form = new SampleStringPageFormProvider()()

  def createView = () => sampleStringPage(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => sampleStringPage(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "SampleStringPage view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like stringPage(createViewUsingForm, messageKeyPrefix, routes.SampleStringPageController.onSubmit(NormalMode).url)
  }
}
