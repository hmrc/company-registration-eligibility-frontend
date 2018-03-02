package models

import play.api.libs.json._

case class SampleQuestionPage (field1: String, field2: String)

object SampleQuestionPage {
  implicit val format = Json.format[SampleQuestionPage]
}
