curl -X POST -H "Content-Type: application/json" localhost:8083/connectors -d @- << EOF 

{
  "name": "debezium-connector-mysql",
  "config": {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "database.hostname": "mysql",
    "database.port": "3306",
    "database.user": "debezium",
    "database.password": "dbz",
    "database.server.id": "184054",
    "database.server.name": "mysqlcdc",
    "database.history.kafka.bootstrap.servers": "kafka:9092",
    "database.history.kafka.topic": "dbhistory.mysqlcdc",
    "include.schema.changes": "true"
  }
}

EOF
