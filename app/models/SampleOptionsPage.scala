package models

import utils.{Enumerable, RadioOption, WithName}

sealed trait SampleOptionsPage

object SampleOptionsPage {

  case object One extends WithName("one") with SampleOptionsPage
  case object Two extends WithName("two") with SampleOptionsPage

  val values: Set[SampleOptionsPage] = Set(
    One, Two
  )

  val options: Set[RadioOption] = values.map {
    value =>
      RadioOption("sampleOptionsPage", value.toString)
  }

  implicit val enumerable: Enumerable[SampleOptionsPage] =
    Enumerable(values.toSeq.map(v => v.toString -> v): _*)
}
