package controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import config.FrontendAppConfig
import forms.SampleYesNoPageFormProvider
import identifiers.SampleYesNoPageId
import models.Mode
import utils.{Navigator, UserAnswers}
import views.html.sampleYesNoPage

import scala.concurrent.Future

class SampleYesNoPageController @Inject()(appConfig: FrontendAppConfig,
                                         override val messagesApi: MessagesApi,
                                         dataCacheConnector: DataCacheConnector,
                                         navigator: Navigator,
                                         authenticate: AuthAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: SampleYesNoPageFormProvider) extends FrontendController with I18nSupport {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.sampleYesNoPage match {
        case None => form
        case Some(value) => form.fill(value)
      }
      Ok(sampleYesNoPage(appConfig, preparedForm, mode))
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(sampleYesNoPage(appConfig, formWithErrors, mode))),
        (value) =>
          dataCacheConnector.save[Boolean](request.externalId, SampleYesNoPageId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(SampleYesNoPageId, mode)(new UserAnswers(cacheMap))))
      )
  }
}
