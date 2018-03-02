#!/bin/bash

echo "Applying migration SampleStringPage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /sampleStringPage               controllers.SampleStringPageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /sampleStringPage               controllers.SampleStringPageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeSampleStringPage                        controllers.SampleStringPageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeSampleStringPage                        controllers.SampleStringPageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "sampleStringPage.title = sampleStringPage" >> ../conf/messages.en
echo "sampleStringPage.heading = sampleStringPage" >> ../conf/messages.en
echo "sampleStringPage.checkYourAnswersLabel = sampleStringPage" >> ../conf/messages.en
echo "sampleStringPage.error.required = Enter sampleStringPage" >> ../conf/messages.en
echo "sampleStringPage.error.length = SampleStringPage must be 100 characters or less" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def sampleStringPage: Option[String] = cacheMap.getEntry[String](SampleStringPageId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def sampleStringPage: Option[AnswerRow] = userAnswers.sampleStringPage map {";\
     print "    x => AnswerRow(\"sampleStringPage.checkYourAnswersLabel\", s\"$x\", false, routes.SampleStringPageController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SampleStringPage completed"
