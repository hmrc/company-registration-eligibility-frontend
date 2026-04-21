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

import config.featureswitch.FeatureSwitching
import connectors.FakeSessionDataCacheConnector
import controllers.actions._
import forms.IdentityVerificationFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import utils.FakeNavigator
import views.html.identityVerification

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class IdentityVerificationControllerSpec extends ControllerSpecBase with FeatureSwitching {

  val view: identityVerification = app.injector.instanceOf[identityVerification]

  val formProvider: IdentityVerificationFormProvider = new IdentityVerificationFormProvider()
  val form: Form[Boolean] = formProvider()

  val controller = new IdentityVerificationController(
    new FakeSessionDataCacheConnector(sessionRepository),
    new FakeNavigator(desiredRoute = routes.IndexController.onPageLoad),
    new FakeSessionAction(messagesControllerComponents),
    formProvider,
    messagesControllerComponents,
    view
  )

  def controllerWithData(data: Option[Boolean]) =
    new IdentityVerificationController(
      new FakeSessionDataCacheConnector(sessionRepository) {
        override def fetchIdentityVerificationFromSession(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[Boolean]] = Future.successful(data)
      },
      new FakeNavigator(routes.IndexController.onPageLoad),
      new FakeSessionAction(messagesControllerComponents),
      formProvider,
      messagesControllerComponents,
      view
    )

  def viewAsString(form: Form[_]): String =
    view(form, NormalMode)(fakeRequest(), messages, frontendAppConfig).toString

  "IdentityVerification Controller" must {

    "return OK and empty view for a GET when no data" in {
      val controller = controllerWithData(None)

      val result = controller.onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form)
    }

    "populate the view correctly on a GET when previously answered true" in {
      val controller = controllerWithData(Some(true))

      val result = controller.onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "populate the view correctly on a GET when previously answered false" in {
      val controller = controllerWithData(Some(false))

      val result = controller.onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form.fill(false))
    }

    "redirect to the next page when valid data is submitted with audit" in {
      val postRequest = fakeRequest("POST").withFormUrlEncodedBody(("value", "true"))

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
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
        new FakeSessionDataCacheConnector(sessionRepository),
        new FakeNavigator(desiredRoute = routes.IndexController.onPageLoad),
        new FakeSessionAction(messagesControllerComponents),
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
        new FakeSessionDataCacheConnector(sessionRepository),
        new FakeNavigator(desiredRoute = routes.IndexController.onPageLoad),
        new FakeSessionAction(messagesControllerComponents),
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




