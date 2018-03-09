#!/bin/bash

echo "Applying migration Eligible"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /eligible                       controllers.EligibleController.onPageLoad()" >> ../conf/app.routes
echo "POST       /eligible                       controllers.EligibleController.onSubmit()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "eligible.title = set-up-company" >> ../conf/messages.en
echo "eligible.heading = set-up-company" >> ../conf/messages.en
echo "eligible.text = You can set up a company with this service" >> ../conf/messages.en
echo "eligible.bullet1 = On the next page you’ll set up a Government Gateway account for the company so you can use the service." >> ../conf/messages.en
echo "eligible.bullet2 = guidance bullet 2" >> ../conf/messages.en

echo "Moving test files from generated-test/ to test/"
rsync -avm --include='*.scala' -f 'hide,! */' ../generated-test/ ../test/
rm -rf ../generated-test/

echo "Migration Eligible completed"
