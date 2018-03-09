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

  def corporateShareholder: Option[AnswerRow] = userAnswers.corporateShareholder map {
    x => AnswerRow("corporateShareholder.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.CorporateShareholderController.onPageLoad().url)
  }

  def corporateOfficer: Option[AnswerRow] = userAnswers.corporateOfficer map {
    x => AnswerRow("corporateOfficer.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.CorporateOfficerController.onPageLoad().url)
  }

  def takingOverBusiness: Option[AnswerRow] = userAnswers.takingOverBusiness map {
    x => AnswerRow("takingOverBusiness.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.TakingOverBusinessController.onPageLoad().url)
  }

  def parentCompany: Option[AnswerRow] = userAnswers.parentCompany map {
    x => AnswerRow("parentCompany.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.ParentCompanyController.onPageLoad().url)
  }

  def tooManyDirectors: Option[AnswerRow] = userAnswers.tooManyDirectors map {
    x => AnswerRow("tooManyDirectors.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.TooManyDirectorsController.onPageLoad().url)
  }

  def ordinaryShares: Option[AnswerRow] = userAnswers.ordinaryShares map {
    x => AnswerRow("ordinaryShares.checkYourAnswersLabel", s"ordinaryShares.$x", true, routes.OrdinarySharesController.onPageLoad(CheckMode).url)
  }
}
