#!/bin/bash

echo "Applying migration SampleYesNoPage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /sampleYesNoPage                       controllers.SampleYesNoPageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /sampleYesNoPage                       controllers.SampleYesNoPageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSampleYesNoPage                       controllers.SampleYesNoPageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSampleYesNoPage                       controllers.SampleYesNoPageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "sampleYesNoPage.title = sampleYesNoPage" >> ../conf/messages.en
echo "sampleYesNoPage.heading = sampleYesNoPage" >> ../conf/messages.en
echo "sampleYesNoPage.checkYourAnswersLabel = sampleYesNoPage" >> ../conf/messages.en
echo "sampleYesNoPage.error.required = Please give an answer for sampleYesNoPage" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def sampleYesNoPage: Option[Boolean] = cacheMap.getEntry[Boolean](SampleYesNoPageId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def sampleYesNoPage: Option[AnswerRow] = userAnswers.sampleYesNoPage map {";\
     print "    x => AnswerRow(\"sampleYesNoPage.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.SampleYesNoPageController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SampleYesNoPage completed"
