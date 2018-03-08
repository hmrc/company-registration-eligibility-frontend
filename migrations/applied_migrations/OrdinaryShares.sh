#!/bin/bash

echo "Applying migration OrdinaryShares"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /ordinaryShares               controllers.OrdinarySharesController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /ordinaryShares               controllers.OrdinarySharesController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOrdinaryShares               controllers.OrdinarySharesController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOrdinaryShares               controllers.OrdinarySharesController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "ordinaryShares.title = Will the company have ‘ordinary shares’?" >> ../conf/messages.en
echo "ordinaryShares.heading = Will the company have ‘ordinary shares’?" >> ../conf/messages.en
echo "ordinaryShares.yes = Yes" >> ../conf/messages.en
echo "ordinaryShares.dk = I don't know dude" >> ../conf/messages.en
echo "ordinaryShares.checkYourAnswersLabel = Will the company have ‘ordinary shares’?" >> ../conf/messages.en
echo "ordinaryShares.error.required = Please give an answer for ordinaryShares" >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def ordinaryShares: Option[OrdinaryShares] = cacheMap.getEntry[OrdinaryShares](OrdinarySharesId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def ordinaryShares: Option[AnswerRow] = userAnswers.ordinaryShares map {";\
     print "    x => AnswerRow(\"ordinaryShares.checkYourAnswersLabel\", s\"ordinaryShares.$x\", true, routes.OrdinarySharesController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration OrdinaryShares completed"
