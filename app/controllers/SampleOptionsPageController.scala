package controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import config.FrontendAppConfig
import forms.SampleOptionsPageFormProvider
import identifiers.SampleOptionsPageId
import models.Mode
import models.SampleOptionsPage
import utils.{Enumerable, Navigator, UserAnswers}
import views.html.sampleOptionsPage

import scala.concurrent.Future

class SampleOptionsPageController @Inject()(
                                        appConfig: FrontendAppConfig,
                                        override val messagesApi: MessagesApi,
                                        dataCacheConnector: DataCacheConnector,
                                        navigator: Navigator,
                                        authenticate: AuthAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        formProvider: SampleOptionsPageFormProvider) extends FrontendController with I18nSupport with Enumerable.Implicits {

  val form = formProvider()

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.sampleOptionsPage match {
        case None => form
        case Some(value) => form.fill(value)
      }
      Ok(sampleOptionsPage(appConfig, preparedForm, mode))
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(sampleOptionsPage(appConfig, formWithErrors, mode))),
        (value) =>
          dataCacheConnector.save[SampleOptionsPage](request.externalId, SampleOptionsPageId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(SampleOptionsPageId, mode)(new UserAnswers(cacheMap))))
      )
  }
}
