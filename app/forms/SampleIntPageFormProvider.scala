package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class SampleIntPageFormProvider @Inject() extends FormErrorHelper with Mappings {

  def apply(): Form[Int] =
    Form(
      "value" -> int(
        "sampleIntPage.error.required",
        "sampleIntPage.error.wholeNumber",
        "sampleIntPage.error.nonNumeric")
          .verifying(inRange(0, 50, "sampleIntPage.error.outOfRange"))
    )
}
