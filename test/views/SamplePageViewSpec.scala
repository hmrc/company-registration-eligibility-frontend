package views

import views.behaviours.ViewBehaviours
import views.html.samplePage

class SamplePageViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "samplePage"

  def createView = () => samplePage(frontendAppConfig)(fakeRequest, messages)

  "SamplePage view" must {
    behave like normalPage(createView, messageKeyPrefix)
  }
}
