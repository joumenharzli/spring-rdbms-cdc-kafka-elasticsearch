echo "Entities in elasticsearch"
curl http://localhost:9200/users/users/_search?pretty

echo "Entities retrived by user query"
curl "http://localhost:8082/api/v1/users/search?page=0&size=10" | json_pp
