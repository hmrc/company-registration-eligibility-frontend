#!/bin/bash

echo "Applying migration CorporateOfficer"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /corporate-director-secretary                                   controllers.CorporateOfficerController.onPageLoad()" >> ../conf/app.routes
echo "POST       /corporate-director-secretary                                   controllers.CorporateOfficerController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "corporateOfficer.title = Do you need to include a ''corporate director'' or ''corporate secretary'' in this application?" >> ../conf/messages.en
echo "corporateOfficer.heading = Do you need to include a ''corporate director'' or ''corporate secretary'' in this application?" >> ../conf/messages.en
echo "corporateOfficer.checkYourAnswersLabel = corporateOfficer" >> ../conf/messages.en
echo "corporateOfficer.error.required = Please give an answer for corporateOfficer" >> ../conf/messages.en
echo "corporateOfficer.text = This is a company director or secretary that''s a corporate bodyy not a human being." >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def corporateOfficer: Option[Boolean] = cacheMap.getEntry[Boolean](CorporateOfficerId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def corporateOfficer: Option[AnswerRow] = userAnswers.corporateOfficer map {";\
     print "    x => AnswerRow(\"corporateOfficer.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.CorporateOfficerController.onPageLoad().url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration CorporateOfficer completed"
