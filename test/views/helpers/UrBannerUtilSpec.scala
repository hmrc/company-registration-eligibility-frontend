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

package views.helpers

import base.SpecBase
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.Lang
import uk.gov.hmrc.hmrcfrontend.views.Aliases.UserResearchBanner
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.language.{Cy, En}
import views.helpers.UrBannerUtil.getUrBanner

class UrBannerUtilSpec extends SpecBase with MockitoSugar {

  "UrBannerUtil" when {
    "getUrBanner" should {
      "return the correct UrBanner in English with hide button" in {
        val urBanner = getUrBanner(hideCloseButton = false)(frontendAppConfig, messages)

        urBanner mustBe new UserResearchBanner(
          language = En,
          url = frontendAppConfig.urBannerBaseUrl,
          hideCloseButton = false
        )
      }

      "return the correct UrBanner in Welsh with hide button" in {
        val welshMessages = messagesApi.preferred(Seq(Lang("cy")))

        val urBanner = getUrBanner(hideCloseButton = false)(frontendAppConfig, welshMessages)

        urBanner mustBe new UserResearchBanner(
          language = Cy,
          url = frontendAppConfig.urBannerBaseUrl + "&Q_Language=CY",
          hideCloseButton = false
        )
      }

      "return the correct UrBanner in English without hide button" in {
        val urBanner = getUrBanner()(frontendAppConfig, messages)

        urBanner mustBe new UserResearchBanner(
          language = En,
          url = frontendAppConfig.urBannerBaseUrl,
          hideCloseButton = true
        )
      }

    }

  }

}