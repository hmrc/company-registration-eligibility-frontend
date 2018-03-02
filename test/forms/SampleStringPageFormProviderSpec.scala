package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class SampleStringPageFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "sampleStringPage.error.required"
  val lengthKey = "sampleStringPage.error.length"
  val maxLength = 100

  val form = new SampleStringPageFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
