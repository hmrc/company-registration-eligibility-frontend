/*
 * Copyright 2026 HM Revenue & Customs
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

package connectors


import play.api.libs.json.Format
import repositories.SessionCacheRepository
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.mongo.cache.DataKey
import utils.UserAnswers

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SessionDataCacheConnector @Inject()(cacheRepository: SessionCacheRepository)(implicit ec: ExecutionContext) {

  import cacheRepository._
  import identifiers.CacheKeys._

  def saveSecureRegisterToSession(secureRegisterVal: Boolean)(implicit hc: HeaderCarrier,
                                                              ec: ExecutionContext): Future[UserAnswers] = {
    putSession[Boolean](secureRegister, secureRegisterVal).flatMap(_ => fetchUserAnswersFromSession)
  }

  def fetchUserAnswersFromSession(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[UserAnswers] = {
    for {
      secureRegister <- getFromSession[Boolean](secureRegister)
      paymentOption <- getFromSession[Boolean](paymentOption)
      identityVerification <- getFromSession[Boolean](identityVerification)
    } yield UserAnswers(secureRegister, paymentOption, identityVerification)
  }

  def fetchIdentityVerificationFromSession(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[Boolean]] = {
    getFromSession[Boolean](identityVerification)
  }

  def fetchPaymentOptionFromSession(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[Boolean]] = {
    getFromSession[Boolean](paymentOption)
  }

  def fetchSecureRegisterFromSession(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[Boolean]] = {
    getFromSession[Boolean](secureRegister)
  }

  def savePaymentOptionToSession(paymentOptionVal: Boolean)(implicit hc: HeaderCarrier,
                                                            ec: ExecutionContext): Future[UserAnswers] = {
    putSession[Boolean](paymentOption, paymentOptionVal).flatMap(_ => fetchUserAnswersFromSession)
  }

  def saveIdentityVerificationToSession(identityVerificationVal: Boolean)(implicit hc: HeaderCarrier,
                                                                          ec: ExecutionContext): Future[UserAnswers] = {
    putSession[Boolean](identityVerification, identityVerificationVal).flatMap(_ => fetchUserAnswersFromSession)
  }
}
