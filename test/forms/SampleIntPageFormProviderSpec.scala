package forms

import forms.behaviours.IntFieldBehaviours
import play.api.data.FormError

class SampleIntPageFormProviderSpec extends IntFieldBehaviours {

  val form = new SampleIntPageFormProvider()()

  ".value" must {

    val fieldName = "value"

    val minimum = 0
    val maximum = 50

    val validDataGenerator = intsInRangeWithCommas(minimum, maximum)

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validDataGenerator
    )

    behave like intField(
      form,
      fieldName,
      nonNumericError  = FormError(fieldName, "sampleIntPage.error.nonNumeric"),
      wholeNumberError = FormError(fieldName, "sampleIntPage.error.wholeNumber")
    )

    behave like intFieldWithRange(
      form,
      fieldName,
      minimum       = minimum,
      maximum       = maximum,
      expectedError = FormError(fieldName, "sampleIntPage.error.outOfRange", Seq(minimum, maximum))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, "sampleIntPage.error.required")
    )
  }
}
