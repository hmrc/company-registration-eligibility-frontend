#!/bin/bash

echo "Applying migration PaymentOption"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /payment-option                                   controllers.PaymentOptionController.onPageLoad()" >> ../conf/app.routes
echo "POST       /payment-option                                   controllers.PaymentOptionController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "paymentOption.title = Are you able to pay for this application using a card or PayPal?" >> ../conf/messages.en
echo "paymentOption.heading = Are you able to pay for this application using a card or PayPal?" >> ../conf/messages.en
echo "paymentOption.checkYourAnswersLabel = paymentOption" >> ../conf/messages.en
echo "paymentOption.error.required = Please give an answer for paymentOption" >> ../conf/messages.en
echo "paymentOption.text = We only accept card or PayPal payments on this service." >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def paymentOption: Option[Boolean] = cacheMap.getEntry[Boolean](PaymentOptionId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def paymentOption: Option[AnswerRow] = userAnswers.paymentOption map {";\
     print "    x => AnswerRow(\"paymentOption.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.PaymentOptionController.onPageLoad().url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration PaymentOption completed"
