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

import controllers.routes
import models.CheckMode
import viewmodels.{AnswerRow, RepeaterAnswerRow, RepeaterAnswerSection}

class CheckYourAnswersHelper(userAnswers: UserAnswers) {

  def sampleYesNoPage: Option[AnswerRow] = userAnswers.sampleYesNoPage map {
    x => AnswerRow("sampleYesNoPage.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.SampleYesNoPageController.onPageLoad(CheckMode).url)
  }

  def sampleStringPage: Option[AnswerRow] = userAnswers.sampleStringPage map {
    x => AnswerRow("sampleStringPage.checkYourAnswersLabel", s"$x", false, routes.SampleStringPageController.onPageLoad(CheckMode).url)
  }

  def sampleQuestionPage: Option[AnswerRow] = userAnswers.sampleQuestionPage map {
    x => AnswerRow("sampleQuestionPage.checkYourAnswersLabel", s"${x.field1} ${x.field2}", false, routes.SampleQuestionPageController.onPageLoad(CheckMode).url)
  }

  def sampleOptionsPage: Option[AnswerRow] = userAnswers.sampleOptionsPage map {
    x => AnswerRow("sampleOptionsPage.checkYourAnswersLabel", s"sampleOptionsPage.$x", true, routes.SampleOptionsPageController.onPageLoad(CheckMode).url)
  }

  def sampleIntPage: Option[AnswerRow] = userAnswers.sampleIntPage map {
    x => AnswerRow("sampleIntPage.checkYourAnswersLabel", s"$x", false, routes.SampleIntPageController.onPageLoad(CheckMode).url)
  }
}
