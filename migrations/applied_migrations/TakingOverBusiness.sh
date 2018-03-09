#!/bin/bash

echo "Applying migration TakingOverBusiness"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /taking-over-business                                   controllers.TakingOverBusinessController.onPageLoad()" >> ../conf/app.routes
echo "POST       /taking-over-business                                   controllers.TakingOverBusinessController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "takingOverBusiness.title = Is the new company taking over another business?" >> ../conf/messages.en
echo "takingOverBusiness.heading = Is the new company taking over another business?" >> ../conf/messages.en
echo "takingOverBusiness.checkYourAnswersLabel = takingOverBusiness" >> ../conf/messages.en
echo "takingOverBusiness.error.required = Please give an answer for takingOverBusiness" >> ../conf/messages.en
echo "takingOverBusiness.text = This includes if it''s:" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def takingOverBusiness: Option[Boolean] = cacheMap.getEntry[Boolean](TakingOverBusinessId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def takingOverBusiness: Option[AnswerRow] = userAnswers.takingOverBusiness map {";\
     print "    x => AnswerRow(\"takingOverBusiness.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.TakingOverBusinessController.onPageLoad().url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration TakingOverBusiness completed"
