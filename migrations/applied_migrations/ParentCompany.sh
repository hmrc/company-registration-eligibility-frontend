#!/bin/bash

echo "Applying migration ParentCompany"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /parent-company                                   controllers.ParentCompanyController.onPageLoad()" >> ../conf/app.routes
echo "POST       /parent-company                                   controllers.ParentCompanyController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "parentCompany.title = Will a parent company control the company you''re setting up?" >> ../conf/messages.en
echo "parentCompany.heading = Will a parent company control the company you''re setting up?" >> ../conf/messages.en
echo "parentCompany.checkYourAnswersLabel = parentCompany" >> ../conf/messages.en
echo "parentCompany.error.required = Please give an answer for parentCompany" >> ../conf/messages.en
echo "parentCompany.text = This is when another company will own at least 75% of the shares in the new company." >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def parentCompany: Option[Boolean] = cacheMap.getEntry[Boolean](ParentCompanyId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def parentCompany: Option[AnswerRow] = userAnswers.parentCompany map {";\
     print "    x => AnswerRow(\"parentCompany.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.ParentCompanyController.onPageLoad().url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration ParentCompany completed"
