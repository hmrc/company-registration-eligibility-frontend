/*
 * Copyright 2019 HM Revenue & Customs
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
  def getAnswer(id: Identifier): Option[Boolean] = cacheMap.getEntry[Boolean](id.toString)

  def secureRegister: Option[Boolean] = cacheMap.getEntry[Boolean](SecureRegisterId.toString)

  def corporateShareholder: Option[Boolean] = cacheMap.getEntry[Boolean](CorporateShareholderId.toString)

  def takingOverBusiness: Option[Boolean] = cacheMap.getEntry[Boolean](TakingOverBusinessId.toString)

  def parentCompany: Option[Boolean] = cacheMap.getEntry[Boolean](ParentCompanyId.toString)

  def paymentOption: Option[Boolean] = cacheMap.getEntry[Boolean](PaymentOptionId.toString)

}
