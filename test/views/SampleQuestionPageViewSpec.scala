package views

import play.api.data.Form
import controllers.routes
import forms.SampleQuestionPageFormProvider
import models.{NormalMode, SampleQuestionPage}
import views.behaviours.QuestionViewBehaviours
import views.html.sampleQuestionPage

class SampleQuestionPageViewSpec extends QuestionViewBehaviours[SampleQuestionPage] {

  val messageKeyPrefix = "sampleQuestionPage"

  override val form = new SampleQuestionPageFormProvider()()

  def createView = () => sampleQuestionPage(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => sampleQuestionPage(frontendAppConfig, form, NormalMode)(fakeRequest, messages)


  "SampleQuestionPage view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithTextFields(createViewUsingForm, messageKeyPrefix, routes.SampleQuestionPageController.onSubmit(NormalMode).url, "field1", "field2")
  }
}
