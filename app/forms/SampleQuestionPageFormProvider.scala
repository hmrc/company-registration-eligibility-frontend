package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms._
import models.SampleQuestionPage

class SampleQuestionPageFormProvider @Inject() extends Mappings {

   def apply(): Form[SampleQuestionPage] = Form(
     mapping(
      "field1" -> text("sampleQuestionPage.error.field1.required")
        .verifying(maxLength(100, "sampleQuestionPage.error.field1.length")),
      "field2" -> text("sampleQuestionPage.error.field2.required")
        .verifying(maxLength(100, "sampleQuestionPage.error.field2.length"))
    )(SampleQuestionPage.apply)(SampleQuestionPage.unapply)
   )
 }
