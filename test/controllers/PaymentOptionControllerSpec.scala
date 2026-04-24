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

import controllers.actions.{DataRetrievalAction, SessionAction}
import forms.PaymentOptionFormProvider
import models.NormalMode
import models.requests.{IdentifierRequest, OptionalDataRequest}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.Form
import play.api.mvc.Results.Redirect
import play.api.mvc._
import play.api.test.Helpers._
import service.SessionDataCacheService
import uk.gov.hmrc.http.HeaderCarrier
import utils.{FakeNavigator, UserAnswers}
import views.html.paymentOption

import scala.concurrent.{ExecutionContext, Future}

class PaymentOptionControllerSpec
  extends ControllerSpecBase
    with MockitoSugar {

  implicit val ec: ExecutionContext = ExecutionContext.global
  implicit val hc: HeaderCarrier = HeaderCarrier()

  val view: paymentOption = app.injector.instanceOf[paymentOption]
  val formProvider = new PaymentOptionFormProvider()
  val form: Form[Boolean] = formProvider()

  val mockService: SessionDataCacheService = mock[SessionDataCacheService]
  val mockResult1: Result = Redirect("/eligibility-for-setting-up-company")
  val mockResult2: Result = Redirect("/eligibility-for-setting-up-company/this-service-has-been-reset")

  def controllerWithData(data: Option[Boolean]): PaymentOptionController = {
    val userAnswers = data.map(v => UserAnswers(paymentOption = Some(v)))

    new PaymentOptionController(
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

    override def parser: BodyParser[AnyContent] =
      stubBodyParser(AnyContentAsEmpty)

    override def invokeBlock[A](
                                 request: Request[A],
                                 block: IdentifierRequest[A] => Future[Result]
                               ): Future[Result] =
      block(IdentifierRequest(request, "internal-id"))
  }

  class FakeDataRetrievalAction(userAnswers: Option[UserAnswers])
    extends DataRetrievalAction(mockService) {
    override protected def transform[A](
                                         request: IdentifierRequest[A]
                                       ): Future[OptionalDataRequest[A]] =
      Future.successful(
        OptionalDataRequest(request.request, request.internalId, userAnswers)
      )
  }

  "PaymentOption Controller" must {

    "return OK and empty view for a GET when no data" in {
      val result = controllerWithData(None).onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form)
    }

    "populate view correctly on GET when previously answered true" in {
      val result = controllerWithData(Some(true)).onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "populate view correctly on GET when previously answered false" in {
      val result = controllerWithData(Some(false)).onPageLoad()(fakeRequest())

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form.fill(false))
    }

    "redirect to next page when valid data is submitted" in {

      when(
        mockService.setPaymentOptionAndRedirectToNextPage(eqTo(true))(
          any[UserAnswers => Call]
        )(any[HeaderCarrier])
      ).thenReturn(Future.successful(mockResult1))

      val request = fakeRequest("POST").withFormUrlEncodedBody("value" -> "true")

      val result = controllerWithData(None).onSubmit()(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.IndexController.onPageLoad.url)
    }

    "return Bad Request when invalid data is submitted" in {
      val request = fakeRequest("POST").withFormUrlEncodedBody("value" -> "invalid")

      val result = controllerWithData(None).onSubmit()(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired when service returns None" in {

      when(
        mockService.setPaymentOptionAndRedirectToNextPage(eqTo(true))(
          any[UserAnswers => Call]
        )(any[HeaderCarrier])
      ).thenReturn(Future.successful(mockResult2))

      val request = fakeRequest("POST").withFormUrlEncodedBody("value" -> "true")

      val result = controllerWithData(None).onSubmit()(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }
  }
}