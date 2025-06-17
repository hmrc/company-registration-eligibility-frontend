/*
 * Copyright 2025 HM Revenue & Customs
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

import config.featureswitch.FeatureSwitch.ScrsIdvEnabled
import config.featureswitch.FeatureSwitching
import connectors.FakeDataCacheConnector
import controllers.actions._
import forms.IdentityVerificationFormProvider
import identifiers.IdentityVerificationId
import models.{IdentityVerificationAudit, NormalMode}
import org.mockito.ArgumentMatchers.{any, anyString}
import org.mockito.{ArgumentMatchers, Mockito}
import org.mockito.Mockito.times
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.data.Form
import play.api.libs.json.{JsBoolean, JsObject}
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import utils.FakeNavigator
import views.html.identityVerification

import scala.concurrent.ExecutionContext.Implicits.global

class IdentityVerificationControllerSpec extends ControllerSpecBase with FeatureSwitching {

  val view: identityVerification = app.injector.instanceOf[identityVerification]
  val mockAuditConnector: AuditConnector = mock[AuditConnector]

  val formProvider: IdentityVerificationFormProvider = new IdentityVerificationFormProvider()
  val form: Form[Boolean] = formProvider()

  val  controller = new IdentityVerificationController(
    new FakeDataCacheConnector(sessionRepository, cascadeUpsert),
    mockAuditConnector,
    new FakeNavigator(desiredRoute = routes.IndexController.onPageLoad),
    new FakeSessionAction(messagesControllerComponents),
    getEmptyCacheMap,
    formProvider,
    messagesControllerComponents,
    view
  )

  def viewAsString(form: Form[_] = form): String = view(form, NormalMode)(fakeRequest(), messages, frontendAppConfig).toString


  "IdentityVerification Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller.onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map(IdentityVerificationId.toString -> JsBoolean(true))
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, validData)), messagesControllerComponents, sessionRepository, cascadeUpsert)

      object Controller extends IdentityVerificationController(
        new FakeDataCacheConnector(sessionRepository, cascadeUpsert),
        mockAuditConnector,
        new FakeNavigator(desiredRoute = routes.IndexController.onPageLoad),
        new FakeSessionAction(messagesControllerComponents),
        getRelevantData,
        formProvider,
        messagesControllerComponents,
        view
      )

      val result = Controller.onPageLoad()(fakeRequest())

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the next page when valid data is submitted with audit" in {
      val postRequest = fakeRequest("POST").withFormUrlEncodedBody(("value", "true"))

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      Mockito.verify(mockAuditConnector, times(1))
        .sendExplicitAudit(ArgumentMatchers.eq("SCRSIDVerification"),
          ArgumentMatchers.eq(IdentityVerificationAudit(Some("Yes"))))(any(), any(), any())
      redirectLocation(result) mustBe Some(routes.IndexController.onPageLoad.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest("POST").withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      object Controller extends IdentityVerificationController(
        new FakeDataCacheConnector(sessionRepository, cascadeUpsert),
        mockAuditConnector,
        new FakeNavigator(desiredRoute = routes.IndexController.onPageLoad),
        new FakeSessionAction(messagesControllerComponents),
        dontGetAnyData,
        formProvider,
        messagesControllerComponents,
        view
      )

      val result = Controller.onPageLoad()(fakeRequest())

      status(result) mustBe OK
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest("POST").withFormUrlEncodedBody(("value", "true"))

      object Controller extends IdentityVerificationController(
        new FakeDataCacheConnector(sessionRepository, cascadeUpsert),
        mockAuditConnector,
        new FakeNavigator(desiredRoute = routes.IndexController.onPageLoad),
        new FakeSessionAction(messagesControllerComponents),
        dontGetAnyData,
        formProvider,
        messagesControllerComponents,
        view
      )
      val result = Controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.IndexController.onPageLoad.url)
    }
  }

}




