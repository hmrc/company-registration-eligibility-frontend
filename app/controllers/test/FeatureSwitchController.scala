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

package controllers.test

import config.FrontendAppConfig
import config.featureswitch.FeatureSwitch.switches
import config.featureswitch.{FeatureSwitch, FeatureSwitching, TakeOversAllowed}
import forms.FeatureSwitchForm
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.test.feature_switch
import javax.inject.{Inject, Singleton}
import models.FeatureSwitchModel
import scala.concurrent.Future


@Singleton
class FeatureSwitchController @Inject()(val appConfig: FrontendAppConfig,
                                        controllerComponents: MessagesControllerComponents,
                                        view: feature_switch
                                       ) extends FrontendController(controllerComponents) with FeatureSwitching with I18nSupport {

  implicit val config: FrontendAppConfig = appConfig

  def show: Action[AnyContent] = Action { implicit request =>
  Ok(view(FeatureSwitchForm.form.fill(
    FeatureSwitchModel(
      takeOversAllowedEnabled = isEnabled(TakeOversAllowed)
    )
  )))
  }

  def submit: Action[AnyContent] = Action.async { implicit req =>
    val submittedData: Set[String] = req.body.asFormUrlEncoded match {
      case None => Set.empty
      case Some(data) => data.keySet
    }

    val frontendFeatureSwitches = submittedData flatMap FeatureSwitch.get

    switches.foreach(fs =>
      if (frontendFeatureSwitches.contains(fs)) {
        enable(fs)
      }
      else {
        disable(fs)
      }

    )

    Future.successful(Redirect(routes.FeatureSwitchController.show))
  }
}
