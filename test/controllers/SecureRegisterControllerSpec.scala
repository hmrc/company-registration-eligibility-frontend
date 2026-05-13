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

import controllers.actions._
import forms.SecureRegisterFormProvider
import models.NormalMode
import models.requests.{DataRequest, IdentifierRequest, OptionalDataRequest}
import play.api.data.Form
import play.api.mvc._
import service.SessionDataCacheService
import utils.{FakeNavigator, UserAnswers}
import views.html.secureRegister

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SecureRegisterControllerSpec extends ControllerSpecBase {

  lazy val formProvider = new SecureRegisterFormProvider()
  lazy val form: Form[Boolean] = formProvider()
  lazy val mockSessionDataCacheService: SessionDataCacheService = app.injector.instanceOf[SessionDataCacheService]
  val view: secureRegister = app.injector.instanceOf[secureRegister]
  val mockSessionAction: SessionAction = app.injector.instanceOf[SessionAction]
  val mockDataRetrieval: DataRetrievalAction = app.injector.instanceOf[DataRetrievalAction]
  val mockDataRequired: DataRequiredAction = app.injector.instanceOf[DataRequiredAction]

  def viewAsString(form: Form[_] = form): String =
    view(form, NormalMode)(fakeRequest(), messages, frontendAppConfig).toString

  def controllerWithData(data: Option[UserAnswers]) =
    new SecureRegisterController(
      new FakeIdentifierAction,
      new FakeDataRetrievalAction(data),
      new FakeDataRequiredAction(data.getOrElse(UserAnswers())),
      mockSessionDataCacheService,
      new FakeNavigator(onwardRoute),
      formProvider,
      messagesControllerComponents,
      view
    )

  import play.api.test.Helpers._

  def onwardRoute: Call = routes.IndexController.onPageLoad

  def controllerWithNoSession() =
    new SecureRegisterController(new FakeIdentifierAction,
      new FakeDataRetrievalAction(None),
      new FakeDataRequiredAction(UserAnswers()),
      mockSessionDataCacheService,
      new FakeNavigator(onwardRoute),
      formProvider,
      messagesControllerComponents,
      view
    )

  class FakeIdentifierAction
    extends SessionAction(messagesControllerComponents) {
    override def invokeBlock[A](
                                 request: Request[A],
                                 block: IdentifierRequest[A] => Future[Result]
                               ): Future[Result] =
      block(IdentifierRequest(request, "internal-id"))
  }

  class FakeDataRetrievalAction(userAnswers: Option[UserAnswers])
    extends DataRetrievalAction(mockSessionDataCacheService) {
    override protected def transform[A](request: IdentifierRequest[A]
                                       ): Future[OptionalDataRequest[A]] =
      Future.successful(
        OptionalDataRequest(request.request, request.internalId, userAnswers)
      )
  }

  class FakeDataRequiredAction(userAnswers: UserAnswers) extends DataRequiredAction() {
    override protected def refine[A](request: OptionalDataRequest[A]): Future[Either[Result, DataRequest[A]]] =
      Future.successful(Right(
        DataRequest(request.request, request.internalId, userAnswers)
      ))
  }

  "SecureRegister Controller" must {

    "return OK and the correct view for a GET" in {
      val controller = controllerWithData(None)

      val result = controller.onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form)
    }

    "populate the view correctly on a GET when previously answered true" in {
      val controller = controllerWithData(Some(UserAnswers(secureRegister = Some(true))))

      val result = controller.onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "populate the view correctly on a GET when previously answered false" in {
      val controller = controllerWithData(Some(UserAnswers(secureRegister = Some(false))))

      val result = controller.onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form.fill(false))
    }

    "redirect to the next page when valid data is submitted" in {
      val controller = controllerWithData(Some(UserAnswers(secureRegister = Some(true))))

      val postRequest =
        fakeRequest("POST").withSession("sessionId" -> "test-session")
          .withFormUrlEncodedBody("value" -> "true")

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val controller = controllerWithData(None)

      val postRequest =
        fakeRequest("POST").withSession("sessionId" -> "test-session")
          .withFormUrlEncodedBody("value" -> "invalid value")

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "go to Session Expired for a GET if no existing data is found" in {
      val controller = controllerWithNoSession()

      val result = controller.onPageLoad()(fakeRequest().withSession("sessionId" -> "test-session"))

      status(result) mustBe OK
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val controller = controllerWithNoSession()

      val postRequest =
        fakeRequest("POST").withSession("sessionId" -> "test-session").withFormUrlEncodedBody("value" -> "true")

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.IndexController.onPageLoad.url)
    }
  }
}
