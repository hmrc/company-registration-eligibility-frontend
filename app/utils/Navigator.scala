/*
 * Copyright 2023 HM Revenue & Customs
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
import models.{Mode, NormalMode}
import play.api.mvc.Call

import javax.inject.{Inject, Singleton}

@Singleton
class Navigator @Inject()() {

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(

    IdentityVerificationId -> { answers =>
      answers.identityVerification match {
        case Some(true) => page(PaymentOptionId)
        case Some(false) => routes.NeedVerifiedIdentityController.onPageLoad()
        case None => page(IdentityVerificationId)
      }
    },

    PaymentOptionId -> { answers =>
      answers.paymentOption match {
        case Some(true) => page(SecureRegisterId)
        case Some(false) => routes.IneligibleController.onPageLoadPayment()
        case None => page(PaymentOptionId)
      }
    },

    SecureRegisterId -> { answers =>
      answers.secureRegister match {
        case Some(false) => page(EligibleId)
        case Some(true) => ineligible(SecureRegisterId)
        case None => page(SecureRegisterId)
      }
    },

    EligibleId -> { answers =>
      answers.eligible match {
        case Some(true) => routes.IndexController.onPageLoad
        case Some(false) => ineligible(EligibleId)
        case None => page(EligibleId)
      }
    }
  )

  def nextPage(id: Identifier, mode: Mode): UserAnswers => Call =
    routeMap.getOrElse(id, _ => routes.IndexController.onPageLoad)

  private def page(pageId: Identifier): Call = pageId match {
    case SecureRegisterId => routes.SecureRegisterController.onPageLoad()
    case EligibleId => routes.EligibleController.onPageLoad()
    case PaymentOptionId => routes.PaymentOptionController.onPageLoad()
    case IdentityVerificationId => routes.IdentityVerificationController.onPageLoad()
    case _ =>
      throw new RuntimeException(s"[Navigator] Unknown pageId: $pageId")
  }

  def pageIdToPageLoad(id: Identifier): Call =
    nextPage(id, NormalMode)(UserAnswers())


  private def ineligible(pageId: Identifier): Call =
    routes.IneligibleController.onPageLoad(pageId.toString)
}