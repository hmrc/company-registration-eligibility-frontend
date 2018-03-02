package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class SampleYesNoPageFormProvider @Inject() extends FormErrorHelper with Mappings {

  def apply(): Form[Boolean] =
    Form(
      "value" -> boolean("sampleYesNoPage.error.required")
    )
}
