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

import repositories.SessionCacheRepository
import uk.gov.hmrc.http.HeaderCarrier
import utils.UserAnswers

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class FakeSessionDataCacheConnector(cacheRepository:
                                    SessionCacheRepository)(implicit ec: ExecutionContext)
  extends SessionDataCacheConnector (cacheRepository) {

  override def fetchIdentityVerificationFromSession(implicit hc: HeaderCarrier,
                                                    ec: ExecutionContext): Future[Option[Boolean]] =
    Future(None)

  override def saveIdentityVerificationToSession(identityVerificationVal: Boolean)(implicit hc: HeaderCarrier,
                                                                                   ec: ExecutionContext): Future[UserAnswers] =
    Future(UserAnswers())

  override def saveSecureRegisterToSession(secureRegisterVal: Boolean)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[UserAnswers] =
    Future(UserAnswers())

  override def fetchSecureRegisterFromSession(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[Boolean]] =
    Future(None)
}
