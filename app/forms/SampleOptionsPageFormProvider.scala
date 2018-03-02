package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import models.SampleOptionsPage

class SampleOptionsPageFormProvider @Inject() extends FormErrorHelper with Mappings {

  def apply(): Form[SampleOptionsPage] =
    Form(
      "value" -> enumerable[SampleOptionsPage]("sampleOptionsPage.error.required")
    )
}
