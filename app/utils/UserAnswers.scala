/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils

import uk.gov.hmrc.http.cache.client.CacheMap
import identifiers._
import models._

class UserAnswers(val cacheMap: CacheMap) extends Enumerable.Implicits {
  def sampleYesNoPage: Option[Boolean] = cacheMap.getEntry[Boolean](SampleYesNoPageId.toString)

  def sampleStringPage: Option[String] = cacheMap.getEntry[String](SampleStringPageId.toString)

  def sampleQuestionPage: Option[SampleQuestionPage] = cacheMap.getEntry[SampleQuestionPage](SampleQuestionPageId.toString)

  def sampleOptionsPage: Option[SampleOptionsPage] = cacheMap.getEntry[SampleOptionsPage](SampleOptionsPageId.toString)

  def sampleIntPage: Option[Int] = cacheMap.getEntry[Int](SampleIntPageId.toString)

}
