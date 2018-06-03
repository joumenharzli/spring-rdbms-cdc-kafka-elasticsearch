while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' localhost:8081/actuator/health)" != "200" ]]; do sleep 5; done

curl -X POST -H "Content-Type: application/json" localhost:8081/api/v1/users -d @- << EOF 

{
  "name": "Joumen",
  "jobs":[{"name":"Software Architect","startDate":"2010-06-03T09:14:56.284Z","endDate":"2018-06-03T09:14:56.284Z","description":"just a test"}],
  "addresses":[{"name":"tunisia"}]
}

EOF
