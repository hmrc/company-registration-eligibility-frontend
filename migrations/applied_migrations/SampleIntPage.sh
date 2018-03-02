#!/bin/bash

echo "Applying migration SampleIntPage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /sampleIntPage               controllers.SampleIntPageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /sampleIntPage               controllers.SampleIntPageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSampleIntPage                        controllers.SampleIntPageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSampleIntPage                        controllers.SampleIntPageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "sampleIntPage.title = sampleIntPage" >> ../conf/messages.en
echo "sampleIntPage.heading = sampleIntPage" >> ../conf/messages.en
echo "sampleIntPage.checkYourAnswersLabel = sampleIntPage" >> ../conf/messages.en
echo "sampleIntPage.error.nonNumeric = Please give an answer for sampleIntPage using numbers" >> ../conf/messages.en
echo "sampleIntPage.error.required = Please give an answer for sampleIntPage" >> ../conf/messages.en
echo "sampleIntPage.error.wholeNumber = Please give an answer for sampleIntPage using whole numbers" >> ../conf/messages.en
echo "sampleIntPage.error.outOfRange = sampleIntPage must be between {0} and {1}" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def sampleIntPage: Option[Int] = cacheMap.getEntry[Int](SampleIntPageId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def sampleIntPage: Option[AnswerRow] = userAnswers.sampleIntPage map {";\
     print "    x => AnswerRow(\"sampleIntPage.checkYourAnswersLabel\", s\"$x\", false, routes.SampleIntPageController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SampleIntPage completed"
