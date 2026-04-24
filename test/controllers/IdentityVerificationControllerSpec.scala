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

package controllers

import config.featureswitch.FeatureSwitching
import controllers.actions.{DataRetrievalAction, SessionAction}
import forms.IdentityVerificationFormProvider
import models.NormalMode
import models.requests.{IdentifierRequest, OptionalDataRequest}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.Form
import play.api.mvc.Results.Redirect
import play.api.mvc._
import service.SessionDataCacheService
import uk.gov.hmrc.http.HeaderCarrier
import utils.{FakeNavigator, UserAnswers}
import views.html.identityVerification

import scala.concurrent.{ExecutionContext, Future}

class IdentityVerificationControllerSpec
  extends ControllerSpecBase
    with FeatureSwitching
    with MockitoSugar {

  implicit val ec: ExecutionContext = ExecutionContext.global
  implicit val hc: HeaderCarrier = HeaderCarrier()

  val view: identityVerification = app.injector.instanceOf[identityVerification]
  val formProvider = new IdentityVerificationFormProvider()
  val form: Form[Boolean] = formProvider()
  val mockResult1: Result = Redirect("/eligibility-for-setting-up-company")
  val mockResult2: Result = Redirect("/eligibility-for-setting-up-company/this-service-has-been-reset")
  private val mockService = mock[SessionDataCacheService]

  def controllerWithData(data: Option[Boolean]): IdentityVerificationController = {
    val userAnswers = data.map(v => UserAnswers(identityVerification = Some(v)))

    new IdentityVerificationController(
      new FakeIdentifierAction,
      new FakeDataRetrievalAction(userAnswers),
      mockService,
      new FakeNavigator(routes.IndexController.onPageLoad),
      formProvider,
      messagesControllerComponents,
      view
    )
  }

  def viewAsString(form: Form[_]): String =
    view(form, NormalMode)(fakeRequest(), messages, frontendAppConfig).toString

  class FakeIdentifierAction
    extends SessionAction(messagesControllerComponents) {
    override def invokeBlock[A](
                                 request: Request[A],
                                 block: IdentifierRequest[A] => Future[Result]
                               ): Future[Result] =
      block(IdentifierRequest(request, "internal-id"))
  }

  import play.api.test.Helpers._

  class FakeDataRetrievalAction(userAnswers: Option[UserAnswers])
    extends DataRetrievalAction(mockService) {
    override protected def transform[A](request: IdentifierRequest[A]
                                       ): Future[OptionalDataRequest[A]] =
      Future.successful(
        OptionalDataRequest(request.request, request.internalId, userAnswers)
      )
  }

  "IdentityVerification Controller" must {

    "return OK and empty view for a GET when no data" in {
      val controller = controllerWithData(None)

      when(
        mockService.setIdentityVerificationAndRedirectToNextPage(eqTo(true))(any())(any())
      ).thenReturn(Future.successful(mockResult1))

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
      val controller = controllerWithData(None)

      when(
        mockService.setIdentityVerificationAndRedirectToNextPage(eqTo(true))(any())(any())
      ).thenReturn(Future.successful(mockResult1))

      val postRequest = fakeRequest("POST").withFormUrlEncodedBody(("value", "true"))

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.IndexController.onPageLoad.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val controller = controllerWithData(None)

      val postRequest = fakeRequest("POST").withFormUrlEncodedBody(("value", "invalid"))
      val boundForm = form.bind(Map("value" -> "invalid"))

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for POST if service returns None" in {
      val controller = controllerWithData(None)

      when(
        mockService.setIdentityVerificationAndRedirectToNextPage(eqTo(true))(any())(any())
      ).thenReturn(Future.successful(mockResult2))

      val postRequest = fakeRequest("POST").withFormUrlEncodedBody(("value", "true"))

      val result = controller.onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }
  }
}



