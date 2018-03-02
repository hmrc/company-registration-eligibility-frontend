#!/bin/bash

echo "Applying migration SamplePage"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /samplePage                       controllers.SamplePageController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "samplePage.title = samplePage" >> ../conf/messages.en
echo "samplePage.heading = samplePage" >> ../conf/messages.en

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration SamplePage completed"
