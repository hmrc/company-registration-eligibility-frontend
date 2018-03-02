package forms

import forms.behaviours.OptionFieldBehaviours
import models.SampleOptionsPage
import play.api.data.FormError

class SampleOptionsPageFormProviderSpec extends OptionFieldBehaviours {

  val form = new SampleOptionsPageFormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "sampleOptionsPage.error.required"

    behave like optionsField[SampleOptionsPage](
      form,
      fieldName,
      validValues  = SampleOptionsPage.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
