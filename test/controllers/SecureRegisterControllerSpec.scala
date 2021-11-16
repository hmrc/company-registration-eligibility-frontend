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

package controllers

import connectors.FakeDataCacheConnector
import controllers.actions._
import forms.SecureRegisterFormProvider
import identifiers.SecureRegisterId
import models.NormalMode
import play.api.data.Form
import play.api.libs.json.JsBoolean
import play.api.mvc.Call
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.FakeNavigator
import views.html.secureRegister

import scala.concurrent.ExecutionContext.Implicits.global

class SecureRegisterControllerSpec extends ControllerSpecBase {

  def onwardRoute: Call = routes.IndexController.onPageLoad

  lazy val formProvider: SecureRegisterFormProvider = new SecureRegisterFormProvider()
  lazy val form: Form[Boolean] = formProvider()

  val view: secureRegister = app.injector.instanceOf[secureRegister]

  object Controller extends SecureRegisterController(
    frontendAppConfig,
    new FakeDataCacheConnector(sessionRepository, cascadeUpsert),
    new FakeNavigator(desiredRoute = onwardRoute),
    new FakeSessionAction(messagesControllerComponents),
    getEmptyCacheMap,
    new DataRequiredAction(messagesControllerComponents),
    formProvider,
    messagesControllerComponents,
    view
  )

  def viewAsString(form: Form[_] = form): String = view(frontendAppConfig, form, NormalMode)(fakeRequest, messages).toString

  "SecureRegister Controller" must {

    "return OK and the correct view for a GET" in {
      val result = Controller.onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map(SecureRegisterId.toString -> JsBoolean(true))
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, validData)), messagesControllerComponents, sessionRepository, cascadeUpsert)

      object Controller extends SecureRegisterController(
        frontendAppConfig,
        new FakeDataCacheConnector(sessionRepository, cascadeUpsert),
        new FakeNavigator(desiredRoute = onwardRoute),
        new FakeSessionAction(messagesControllerComponents),
        getRelevantData,
        new DataRequiredAction(messagesControllerComponents),
        formProvider,
        messagesControllerComponents,
        view
      )

      val result = Controller.onPageLoad()(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = Controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = Controller.onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      object Controller extends SecureRegisterController(
        frontendAppConfig,
        new FakeDataCacheConnector(sessionRepository, cascadeUpsert),
        new FakeNavigator(desiredRoute = onwardRoute),
        new FakeSessionAction(messagesControllerComponents),
        dontGetAnyData,
        new DataRequiredAction(messagesControllerComponents),
        formProvider,
        messagesControllerComponents,
        view
      )

      val result = Controller.onPageLoad()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      object Controller extends SecureRegisterController(
        frontendAppConfig,
        new FakeDataCacheConnector(sessionRepository, cascadeUpsert),
        new FakeNavigator(desiredRoute = onwardRoute),
        new FakeSessionAction(messagesControllerComponents),
        dontGetAnyData,
        new DataRequiredAction(messagesControllerComponents),
        formProvider,
        messagesControllerComponents,
        view
      )
      val result = Controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }
  }
}
