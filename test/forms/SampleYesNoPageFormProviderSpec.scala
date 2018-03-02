package forms

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class SampleYesNoPageFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "sampleYesNoPage.error.required"
  val invalidKey = "error.boolean"

  val form = new SampleYesNoPageFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
