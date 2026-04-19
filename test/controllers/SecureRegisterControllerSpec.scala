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

package controllers

import connectors.FakeSessionDataCacheConnector
import controllers.actions._
import forms.SecureRegisterFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import utils.{FakeNavigator, UserAnswers}
import views.html.secureRegister

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class SecureRegisterControllerSpec extends ControllerSpecBase {

  lazy val formProvider: SecureRegisterFormProvider = new SecureRegisterFormProvider()
  lazy val form: Form[Boolean] = formProvider()
  val view: secureRegister = app.injector.instanceOf[secureRegister]

  def onwardRoute: Call = routes.IndexController.onPageLoad

  def viewAsString(form: Form[_] = form): String = view(form, NormalMode)(fakeRequest(), messages, frontendAppConfig).toString

  def controllerWithData(data: Option[Boolean]) =
    new SecureRegisterController(
      new FakeSessionDataCacheConnector(sessionRepository) {
        override def fetchSecureRegisterFromSession(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[Boolean]] = Future.successful(data)

        override def saveSecureRegisterToSession(paymentOptionVal: Boolean)(implicit hc: HeaderCarrier,
                                                                            ec: ExecutionContext): Future[UserAnswers] =
          Future.successful(UserAnswers())
      },
      new FakeNavigator(routes.IndexController.onPageLoad),
      new FakeSessionAction(messagesControllerComponents),
      formProvider,
      messagesControllerComponents,
      view
    )

  object Controller extends SecureRegisterController(
    new FakeSessionDataCacheConnector(sessionRepository),
    new FakeNavigator(desiredRoute = onwardRoute),
    new FakeSessionAction(messagesControllerComponents),
    formProvider,
    messagesControllerComponents,
    view
  )

  "SecureRegister Controller" must {

    "return OK and the correct view for a GET" in {
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

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest("POST").withFormUrlEncodedBody(("value", "true"))

      val result = controllerWithData(Some(true)).onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest("POST").withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = Controller.onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "go to Session Expired for a GET if no existing data is found" in {

      object Controller extends SecureRegisterController(
        new FakeSessionDataCacheConnector(sessionRepository),
        new FakeNavigator(desiredRoute = onwardRoute),
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

      object Controller extends SecureRegisterController(
        new FakeSessionDataCacheConnector(sessionRepository),
        new FakeNavigator(desiredRoute = onwardRoute),
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
