#!/bin/bash

echo "Applying migration Ineligible"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /ineligible                       controllers.IneligibleController.onPageLoad()" >> ../conf/app.routes
echo "POST       /ineligible                       controllers.IneligibleController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "ineligible.title = You need to use another service" >> ../conf/messages.en
echo "ineligible.heading = You need to use another service" >> ../conf/messages.en
echo "ineligible.text = To set up a company with more than 5 company officers, you’ll need to use another service." >> ../conf/messages.en
echo "ineligible.bullet1 = guidance bullet 1" >> ../conf/messages.en
echo "ineligible.bullet2 = guidance bullet 2" >> ../conf/messages.en

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration Ineligible completed"
