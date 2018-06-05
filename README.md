# Denormalize and Index MySQL database with ElasticSearch and Kafka

## Overview
* This example demonstrates how we can create a real time search engine for entities in an SQL database using <b>Change Data Capture</b>.
* The same mechanism can be applied to a NonSQL database like MongoDb or to manage a cache database.
* This example relies on <b>Debezium Connector for MySQL</b> and <b>Kafka Connect</b>.
* The microservices was developed using Spring boot 2.

## Architecture
<img src="https://image.ibb.co/de2S8T/archi.png" />

## Running the example

### Requirements
This example requires:
* JDK 1.8.0
* Docker
* Docker-compose > 3.6

### Steps
Start by adding the hostname kafka with ip address 127.0.0.1 in the hosts file by running
```bash
echo "127.0.0.1     kafka" >> /etc/hosts
``` 
<b>Note: </b>This step will be removed in the future when the microservices will be running in the same docker network as Kafka.

Then run the containers using 
```bash
docker-compose up -d
```
you can exclude ElasticSearch if you want to keep some memory.

After the successful start of the containers, activate the Kafka connector by running the command
```bash
sh activate_connector.sh
```

When the connector is successfully added, launch the Command Microservice
```bash
cd command/
mvnw spring-boot:run
```

Then we need to add an entity we can run the command
```bash
test_command.sh
```
Add, update and remove endpoints was implemented in the Command Microservice but this will not be documented in this tutorial you can try them by yourself. After running the command it's possible to stop this Microservice and MySQL container.

If you stopped ElasticSearch please run it to continue.
When the new entity is added, Start the denormalizer Microservice using the command
```bash
cd denormalizer/
mvnw spring-boot:run
```
This will process the events received from Kafka and add entities to ElasticSearch.

If the events were successfully processed. Run the Query Microservice using the command
```bash
cd query/
mvnw spring-boot:run
```

Then verify that everything is working using the command
```bash
test_query.sh
```
 
