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

import base.SpecBase
import controllers.routes
import identifiers._
import models._
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.JsBoolean
import uk.gov.hmrc.http.cache.client.CacheMap

class NavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator

  "Navigator" when {
    "in Normal mode" must {
      "go to Index from an identifier that doesn't exist in the route map" in {
        case object UnknownIdentifier extends Identifier
        navigator.nextPage(UnknownIdentifier, NormalMode)(mock[UserAnswers]) mustBe routes.IndexController.onPageLoad
      }
    }
  }

  "pageIdToPageLoad" must {
    "load a page" when {
      Seq(
        SecureRegisterId -> routes.SecureRegisterController.onPageLoad(),
        EligibleId -> routes.EligibleController.onPageLoad(),
        PaymentOptionId -> routes.PaymentOptionController.onPageLoad(),
        IdentityVerificationId -> routes.IdentityVerificationController.onPageLoad()
      ) foreach { case (id, page) =>
        s"given an ID of ${id.toString} must go to ${page.url}" in {
          navigator.pageIdToPageLoad(id).url must include(page.url)
        }
      }
    }

    "throw a run time exception" when {
      "given an invalid ID" in {
        sealed class FakeID extends Identifier

        intercept[RuntimeException](navigator.pageIdToPageLoad(new FakeID))
      }
    }
  }

  "nextOnFalse" must {
    "return an ID and function to the next page" when {
      "given a start page id and end page id when the answer provided is false" in {
        val res = navigator.nextOn(PaymentOptionId, SecureRegisterId)
        val userAnswers = new UserAnswers(CacheMap("id", Map(PaymentOptionId.toString -> JsBoolean(false))))
        val res1 = navigator.nextOn(IdentityVerificationId, PaymentOptionId)
        val userAnswers1 = new UserAnswers(CacheMap("id", Map(IdentityVerificationId.toString -> JsBoolean(false))))
        res._1 mustBe PaymentOptionId
        res1._1 mustBe IdentityVerificationId
      }
    }
  }
}
