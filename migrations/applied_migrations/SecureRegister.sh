#!/bin/bash

echo "Applying migration SecureRegister"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /secure-register-form                                   controllers.SecureRegisterController.onPageLoad()" >> ../conf/app.routes
echo "POST       /secure-register-form                                   controllers.SecureRegisterController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "secureRegister.title = Has anyone on this application ever sent a Secure Register Form asking Companies House to protect their home address information?" >> ../conf/messages.en
echo "secureRegister.heading = Has anyone on this application ever sent a Secure Register Form asking Companies House to protect their home address information?" >> ../conf/messages.en
echo "secureRegister.checkYourAnswersLabel = secureRegister" >> ../conf/messages.en
echo "secureRegister.error.required = Please give an answer for secureRegister" >> ../conf/messages.en
echo "secureRegister.text = This includes any:" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def secureRegister: Option[Boolean] = cacheMap.getEntry[Boolean](SecureRegisterId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def secureRegister: Option[AnswerRow] = userAnswers.secureRegister map {";\
     print "    x => AnswerRow(\"secureRegister.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.SecureRegisterController.onPageLoad().url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SecureRegister completed"
