sbt validate
sbt -Dfrontend -Dplay.http.router=testOnlyDoNotUseInAppConf.Routes "run 9972"
echo "running here http://localhost:9972/eligibility-for-setting-up-company"