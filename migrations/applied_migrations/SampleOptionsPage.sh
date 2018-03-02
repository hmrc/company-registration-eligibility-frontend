#!/bin/bash

echo "Applying migration SampleOptionsPage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /sampleOptionsPage               controllers.SampleOptionsPageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /sampleOptionsPage               controllers.SampleOptionsPageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSampleOptionsPage               controllers.SampleOptionsPageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSampleOptionsPage               controllers.SampleOptionsPageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "sampleOptionsPage.title = Sample options page" >> ../conf/messages.en
echo "sampleOptionsPage.heading = Sample options page" >> ../conf/messages.en
echo "sampleOptionsPage.one = One" >> ../conf/messages.en
echo "sampleOptionsPage.two = Two" >> ../conf/messages.en
echo "sampleOptionsPage.checkYourAnswersLabel = Sample options page" >> ../conf/messages.en
echo "sampleOptionsPage.error.required = Please give an answer for sampleOptionsPage" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def sampleOptionsPage: Option[SampleOptionsPage] = cacheMap.getEntry[SampleOptionsPage](SampleOptionsPageId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def sampleOptionsPage: Option[AnswerRow] = userAnswers.sampleOptionsPage map {";\
     print "    x => AnswerRow(\"sampleOptionsPage.checkYourAnswersLabel\", s\"sampleOptionsPage.$x\", true, routes.SampleOptionsPageController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SampleOptionsPage completed"
