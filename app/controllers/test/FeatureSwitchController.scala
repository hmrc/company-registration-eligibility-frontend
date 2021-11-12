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

package controllers.test

import config.FrontendAppConfig
import config.featureswitch.FeatureSwitch.switches
import config.featureswitch.{FeatureSwitch, FeatureSwitching}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import play.twirl.api.Html
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.html.test.feature_switch

import javax.inject.{Inject, Singleton}
import scala.collection.immutable.ListMap
import scala.concurrent.Future


@Singleton
class FeatureSwitchController @Inject()(val appConfig: FrontendAppConfig,
                                        controllerComponents: MessagesControllerComponents,
                                        view: feature_switch
                                       ) extends FrontendController(controllerComponents) with FeatureSwitching with I18nSupport {

  implicit val config: FrontendAppConfig = appConfig

  private def view(switchNames: Map[FeatureSwitch, Boolean])(implicit request: Request[_]): Html =
    view(
      switchNames = switchNames,
      routes.FeatureSwitchController.submit()
    )


  def show: Action[AnyContent] = Action { implicit request =>
    val featureSwitches = ListMap(switches.toSeq sortBy (_.displayText) map (switch => switch -> isEnabled(switch)): _*)
    Ok(view(featureSwitches))
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

    Future.successful(Redirect(routes.FeatureSwitchController.show()))
  }
}
