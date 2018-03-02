package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class SampleStringPageFormProvider @Inject() extends FormErrorHelper with Mappings {

  def apply(): Form[String] =
    Form(
      "value" -> text("sampleStringPage.error.required")
        .verifying(maxLength(100, "sampleStringPage.error.length"))
    )
}
