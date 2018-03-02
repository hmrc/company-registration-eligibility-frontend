#!/bin/bash

echo "Applying migration SampleQuestionPage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /sampleQuestionPage                       controllers.SampleQuestionPageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /sampleQuestionPage                       controllers.SampleQuestionPageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSampleQuestionPage                       controllers.SampleQuestionPageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSampleQuestionPage                       controllers.SampleQuestionPageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "sampleQuestionPage.title = sampleQuestionPage" >> ../conf/messages.en
echo "sampleQuestionPage.heading = sampleQuestionPage" >> ../conf/messages.en
echo "sampleQuestionPage.field1 = Field 1" >> ../conf/messages.en
echo "sampleQuestionPage.field2 = Field 2" >> ../conf/messages.en
echo "sampleQuestionPage.checkYourAnswersLabel = sampleQuestionPage" >> ../conf/messages.en
echo "sampleQuestionPage.error.field1.required = Enter field1" >> ../conf/messages.en
echo "sampleQuestionPage.error.field2.required = Enter field2" >> ../conf/messages.en
echo "sampleQuestionPage.error.field1.length = field1 must be 100 characters or less" >> ../conf/messages.en
echo "sampleQuestionPage.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def sampleQuestionPage: Option[SampleQuestionPage] = cacheMap.getEntry[SampleQuestionPage](SampleQuestionPageId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def sampleQuestionPage: Option[AnswerRow] = userAnswers.sampleQuestionPage map {";\
     print "    x => AnswerRow(\"sampleQuestionPage.checkYourAnswersLabel\", s\"${x.field1} ${x.field2}\", false, routes.SampleQuestionPageController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SampleQuestionPage completed"
