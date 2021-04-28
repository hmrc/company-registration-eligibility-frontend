/*
 * Copyright 2021 HM Revenue & Customs
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

package filters

import akka.stream.Materializer
import play.api.http.HeaderNames
import play.api.mvc._
import uk.gov.hmrc.http.{SessionKeys, HeaderNames => HMRCHeaderNames}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SessionIdFilter @Inject()(override val mat: Materializer,
                                sessionCookieBaker: SessionCookieBaker
                               )(implicit ec: ExecutionContext) extends Filter {

  override def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {

    def sessionId: String = s"session-${UUID.randomUUID().toString}"

    if (rh.session.get(SessionKeys.sessionId).isEmpty) {

      val cookies: String = {

        val session: Session =
          rh.session + (SessionKeys.sessionId -> sessionId)

        val cookies =
          rh.cookies ++ Seq(Session.encodeAsCookie(session))

        Cookies.encodeCookieHeader(cookies.toSeq)
      }

      val headers = rh.headers.add(
        HMRCHeaderNames.xSessionId -> sessionId,
        HeaderNames.COOKIE -> cookies
      )

      f(rh.withHeaders(headers)).map {
        result =>

          val cookies =
            Cookies.fromSetCookieHeader(result.header.headers.get(HeaderNames.SET_COOKIE))

          val session = Session.decodeFromCookie(cookies.get(sessionCookieBaker.COOKIE_NAME)).data
            .foldLeft(rh.session) {
              case (m, n) =>
                m + n
            }

          result.withSession(session + (SessionKeys.sessionId -> sessionId))
      }
    } else {
      f(rh)
    }
  }
}

