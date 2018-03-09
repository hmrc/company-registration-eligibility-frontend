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

import javax.inject.{Inject, Singleton}

import play.api.mvc.Call
import controllers.routes
import identifiers._
import models.{CheckMode, Mode, NormalMode, OrdinaryShares}

@Singleton
class Navigator @Inject()() {

  type Navigation = Tuple2[Identifier, UserAnswers => Call]

  private def pageIdToPageLoad(pageId: Identifier) = pageId match {
    case TooManyDirectorsId => routes.TooManyDirectorsController.onPageLoad()
    case OrdinarySharesId => routes.OrdinarySharesController.onPageLoad(NormalMode)
    case ParentCompanyId => routes.ParentCompanyController.onPageLoad()
    case TakingOverBusinessId => routes.TakingOverBusinessController.onPageLoad()
    case _ => ???
  }

  private def getYesNoAnswer(answers: UserAnswers, pageId: Identifier): Option[Boolean] = pageId match {
    case TooManyDirectorsId => answers.tooManyDirectors
    case ParentCompanyId => answers.parentCompany
    case TakingOverBusinessId => answers.takingOverBusiness
    case _ => ???
  }

  private def ineligiblePage(pageId: Identifier) = routes.IneligibleController.onPageLoad(pageId.toString)

  private def nextOnFalse(fromPage: Identifier, toPage: Identifier): Navigation = {
    fromPage -> {
      answers => getYesNoAnswer(answers, fromPage) match {
        case Some(false) => pageIdToPageLoad(toPage)
        case _ => ineligiblePage(fromPage)
      }
    }
  }

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(

    nextOnFalse(TooManyDirectorsId, OrdinarySharesId),
    OrdinarySharesId -> { answers => answers.ordinaryShares match {
      case Some(OrdinaryShares.No) => ineligiblePage(OrdinarySharesId)
      case _ => pageIdToPageLoad(ParentCompanyId)
    }
    },
    nextOnFalse(ParentCompanyId, TakingOverBusinessId),
    nextOnFalse(TakingOverBusinessId, TakingOverBusinessId)

  )

  private val editRouteMap: Map[Identifier, UserAnswers => Call] = Map(

  )

  def nextPage(id: Identifier, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMap.getOrElse(id, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      editRouteMap.getOrElse(id, _ => routes.CheckYourAnswersController.onPageLoad())
  }
}
