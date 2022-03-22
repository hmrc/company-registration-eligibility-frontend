/*
 * Copyright 2022 HM Revenue & Customs
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
import identifiers._
import models.Mode
import play.api.mvc.Call

import javax.inject.{Inject, Singleton}

@Singleton
class Navigator @Inject()() {

  private[utils] def pageIdToPageLoad(pageId: Identifier): Call = pageId match {
    case TakingOverBusinessId => routes.TakingOverBusinessController.onPageLoad()
    case SecureRegisterId => routes.SecureRegisterController.onPageLoad()
    case EligibleId => routes.EligibleController.onPageLoad()
    case PaymentOptionId => routes.EligibleController.onPageLoad()
    case _ => throw new RuntimeException(s"[Navigator] [pageIdToPageLoad] Could not load page for pageId: $pageId")
  }

  private def ineligiblePage(pageId: Identifier) = routes.IneligibleController.onPageLoad(pageId.toString)

  private[utils] def nextOn(fromPage: Identifier, toPage: Identifier, condition: Boolean = false): (Identifier, UserAnswers => Call) = {
    fromPage -> {
      _.getAnswer(fromPage) match {
        case Some(`condition`) => pageIdToPageLoad(toPage)
        case _ => ineligiblePage(fromPage)
      }
    }
  }

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(
    PaymentOptionId -> {
      _.paymentOption match {
        case Some(true) => pageIdToPageLoad(TakingOverBusinessId)
        case _ => routes.IneligibleController.onPageLoadPayment()
      }
    },
    nextOn(TakingOverBusinessId, SecureRegisterId),
    nextOn(SecureRegisterId, EligibleId),
    nextOn(EligibleId, EligibleId)
  )

  def nextPage(id: Identifier, mode: Mode): UserAnswers => Call =
    routeMap.getOrElse(id, _ => routes.IndexController.onPageLoad)
}
