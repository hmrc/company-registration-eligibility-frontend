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

package views

import config.featureswitch.FeatureSwitch.ScrsIdvEnabled
import config.featureswitch.FeatureSwitching
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.behaviours.ViewBehaviours
import views.html.beforeYouStart

class BeforeYouStartViewSpec extends ViewBehaviours with FeatureSwitching {

  val messageKeyPrefix = "beforeYouStart"
  val view: beforeYouStart = app.injector.instanceOf[beforeYouStart]

  def createView: () => Html = () => view()(fakeRequest(), messages, frontendAppConfig)

  "BeforeYouStart view" when {
    "Normal view with no feature enabled" must {
      disable(ScrsIdvEnabled)
      behave like normalPage(createView, messageKeyPrefix)
    }
  }

  "beforeYouStart view" when {
    "New feature SCRS IV enabled, should show new content" in {
      enable(ScrsIdvEnabled)
      val beforeYouStartView = createView
      val doc = asDocument(beforeYouStartView())

      val nav = doc.getElementsByClass("govuk-header").text()
      nav must include(messages("site.service_name"))
      doc.title must include(messages(s"$messageKeyPrefix.title-idv"))
      assertPageTitleEqualsMessage(doc, s"$messageKeyPrefix.heading-idv")
      doc.getElementsByTag("p").last().text() mustBe "We’ll also ask you to confirm that all the company directors and people with" +
        " significant control (PSCs) have verified their identity for Companies House."
      doc.getElementsByClass("govuk-button").text() mustBe "Continue"
    }
  }
}
