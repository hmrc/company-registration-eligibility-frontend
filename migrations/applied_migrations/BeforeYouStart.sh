#!/bin/bash

echo "Applying migration BeforeYouStart"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /beforeYouStart                       controllers.BeforeYouStartController.onPageLoad()" >> ../conf/app.routes
echo "POST       /beforeYouStart                       controllers.BeforeYouStartController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "beforeYouStart.title = Check before you start" >> ../conf/messages.en
echo "beforeYouStart.heading = Check before you start" >> ../conf/messages.en
echo "beforeYouStart.text = We’re going to ask some questions. Your answers will:" >> ../conf/messages.en
echo "beforeYouStart.bullet1 = show whether you can use this service to register a limited company and set up Corporation Tax" >> ../conf/messages.en
echo "beforeYouStart.bullet2 = take you to another service if you’re not eligible to use this one" >> ../conf/messages.en

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration BeforeYouStart completed"
