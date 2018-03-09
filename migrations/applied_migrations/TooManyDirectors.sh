#!/bin/bash

echo "Applying migration TooManyDirectors"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /register-more-than-five-directors                                   controllers.TooManyDirectorsController.onPageLoad()" >> ../conf/app.routes
echo "POST       /register-more-than-five-directors                                   controllers.TooManyDirectorsController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "tooManyDirectors.title = Do you need to include more than 5 company officers in this application?" >> ../conf/messages.en
echo "tooManyDirectors.heading = Do you need to include more than 5 company officers in this application?" >> ../conf/messages.en
echo "tooManyDirectors.checkYourAnswersLabel = tooManyDirectors" >> ../conf/messages.en
echo "tooManyDirectors.error.required = Please give an answer for tooManyDirectors" >> ../conf/messages.en
echo "tooManyDirectors.text = Company officers are directors or secretaries who represent the company and make key decisions about how it’s run." >> ../conf/messages.en

echo "Adding helper line into UserAnswers"
awk '/class/ {\
     print;\
     print "  def tooManyDirectors: Option[Boolean] = cacheMap.getEntry[Boolean](TooManyDirectorsId.toString)";\
     print "";\
     next }1' ../app/utils/UserAnswers.scala > tmp && mv tmp ../app/utils/UserAnswers.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def tooManyDirectors: Option[AnswerRow] = userAnswers.tooManyDirectors map {";\
     print "    x => AnswerRow(\"tooManyDirectors.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.TooManyDirectorsController.onPageLoad().url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration TooManyDirectors completed"
