#!/bin/bash

echo "Applying migration CorporateShareholder"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /corporate-shareholder                                   controllers.CorporateShareholderController.onPageLoad()" >> ../conf/app.routes
echo "POST       /corporate-shareholder                                   controllers.CorporateShareholderController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "corporateShareholder.title = Do you need to include a ‘corporate shareholder’ in this application?" >> ../conf/messages.en
echo "corporateShareholder.heading = Do you need to include a ‘corporate shareholder’ in this application?" >> ../conf/messages.en
echo "corporateShareholder.checkYourAnswersLabel = corporateShareholder" >> ../conf/messages.en
echo "corporateShareholder.error.required = Please give an answer for corporateShareholder" >> ../conf/messages.en
echo "corporateShareholder.text = This is a shareholder that’s a corporate body not a human being." >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def corporateShareholder: Option[Boolean] = cacheMap.getEntry[Boolean](CorporateShareholderId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def corporateShareholder: Option[AnswerRow] = userAnswers.corporateShareholder map {";\
     print "    x => AnswerRow(\"corporateShareholder.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.CorporateShareholderController.onPageLoad().url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration CorporateShareholder completed"
