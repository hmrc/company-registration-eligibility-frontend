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

package config.featureswitch

sealed trait FeatureSwitch {
  val name: String
  val displayText: String
}

object FeatureSwitch {
  val prefix = "feature-switch"

  val switches: Set[FeatureSwitch] = Set(
    WelshEnabled,
    ScrsIdvEnabled
  )

  def apply(str: String): FeatureSwitch = {
    switches.find(_.name == str) match {
      case Some(switch) => switch
      case None => throw new IllegalArgumentException("Invalid feature switch: " + str)
    }
  }

  def get(str: String): Option[FeatureSwitch] = {
    switches.find(_.name == str)
  }

  case object WelshEnabled extends FeatureSwitch {
    override val name: String = s"$prefix.enable-welsh"
    override val displayText: String = "Enable Welsh Language"
  }

  case object ScrsIdvEnabled extends FeatureSwitch {
    override val name: String = s"$prefix.scrs-idv"
    override val displayText: String = "Enable SCRS IDV"
  }
}
