package controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import config.FrontendAppConfig
import forms.SampleQuestionPageFormProvider
import identifiers.SampleQuestionPageId
import models.{Mode, SampleQuestionPage}
import utils.{Navigator, UserAnswers}
import views.html.sampleQuestionPage

import scala.concurrent.Future

class SampleQuestionPageController @Inject()(appConfig: FrontendAppConfig,
                                                  override val messagesApi: MessagesApi,
                                                  dataCacheConnector: DataCacheConnector,
                                                  navigator: Navigator,
                                                  authenticate: AuthAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  formProvider: SampleQuestionPageFormProvider) extends FrontendController with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.sampleQuestionPage match {
        case None => form
        case Some(value) => form.fill(value)
      }
      Ok(sampleQuestionPage(appConfig, preparedForm, mode))
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(sampleQuestionPage(appConfig, formWithErrors, mode))),
        (value) =>
          dataCacheConnector.save[SampleQuestionPage](request.externalId, SampleQuestionPageId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(SampleQuestionPageId, mode)(new UserAnswers(cacheMap))))
      )
  }
}
