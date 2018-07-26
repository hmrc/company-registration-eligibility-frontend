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

package helpers

import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.mvc.{RequestHeader, Result}
import play.mvc.Http
import play.api.mvc.Results.Ok
import uk.gov.hmrc.play.language.LanguageUtils

import scala.io.Source


object MockMessages {
  def apply(): MockMessagesApi = MockEnglishMessages
  def apply(request: RequestHeader): Messages = MockEnglishMessages.preferred(request)
}
object MockEnglishMessages extends MockMessagesApi {
  val messagesFilename = "messages.en"
}

object MockWelshMessages extends MockMessagesApi {
  val messagesFilename = "messages.cy"
}

trait MockMessagesApi extends MessagesApi {
  val messagesFilename: String
  lazy val messagesMap: Map[String, String] = Source.fromFile(s"conf/$messagesFilename").getLines().foldLeft(Map[String, String]()){
    case (map, line) => val splitLine = line.replaceAll("''", "'").split("=", 2).map(_.trim)
      map + (splitLine.head -> splitLine.last)
  }

  override def messages: Map[String, Map[String, String]] = Map()
  override def preferred(candidates: Seq[Lang]): Messages = Messages(Lang("en"), MockEnglishMessages)
  override def preferred(request: RequestHeader): Messages = LanguageUtils.getCurrentLang(request).code match {
    case "en" => Messages(Lang("en"), MockEnglishMessages)
    case "cy" => Messages(Lang("cy"), MockWelshMessages)
  }
  override def preferred(request: Http.RequestHeader): Messages = LanguageUtils.getCurrentLang(request._underlyingHeader()).code match {
    case "en" => Messages(Lang("en"), MockEnglishMessages)
    case "cy" => Messages(Lang("cy"), MockWelshMessages)
  }

  override def setLang(result: Result, lang: Lang): Result = Ok
  override def clearLang(result: Result): Result = Ok
  override def apply(key: String, args: Any*)(implicit lang: Lang): String = args.zipWithIndex.foldLeft(messagesMap.getOrElse(key, key)){
    case (message, (argument, index)) => message.replaceAll(s"\\{$index\\}", s"$argument")
  }
  override def apply(keys: Seq[String], args: Any*)(implicit lang: Lang): String = keys.mkString(" ")
  override def translate(key: String, args: Seq[Any])(implicit lang: Lang): Option[String] = None
  override def isDefinedAt(key: String)(implicit lang: Lang): Boolean = false
  override def langCookieName: String = "en"
  override def langCookieSecure: Boolean = false
  override def langCookieHttpOnly: Boolean = false
}
