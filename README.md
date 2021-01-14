# Denormalize and Index MySQL database with ElasticSearch and Kafka

## Overview
* This example demonstrates how we can create a real time search engine for entities in an SQL database using <b>Change Data Capture</b>.
* The same mechanism can be applied to a NonSQL database like MongoDb or to manage a cache database.
* This example relies on <b>Debezium Connector for MySQL</b> and <b>Kafka Connect</b>.
* The microservices was developed using Spring boot 2.

## Donations
If you find my work useful, consider donating to support it :)
### <img alt="Image of Ethereum" width="25" height="25" src="https://github.com/joumenharzli/donations/blob/main/Ethereum-icon.png?raw=true"> Ethereum
You can simply scan this QR code to get my Ethereum address

<img alt="My QR Code" width="200" height="200" src="https://github.com/joumenharzli/donations/blob/main/ethereum.png?raw=true">

## Architecture
<img src="https://image.ibb.co/de2S8T/archi.png" />

## Running the example

### Requirements
This example requires:
* JDK 1.8
* Docker >= 17
* Docker-compose >= 3.6

### Steps
Start by building the Spring projects using
```bash
sh build.sh
``` 

Next, run the containers Command, MySQL, Kafka, Zookeeper, Kafka Connect using 
```bash
docker-compose up --build -d mysql user-command kafka-connect
```

<b>Note: </b> You can start all the project containers but this costs a lots of memory. You can simply use
```bash
docker-compose up --build -d
```

After the successful start of the containers, activate the Kafka connector by running the command
```bash
sh activate_connector.sh
```
If there are entities in the database Debezium will create a Snapshot.

When the connector is successfully added, you can add an entity by running the command
```bash
sh test_command.sh
```
Add, update and remove endpoints was implemented in the Command Microservice but this will not be documented in this tutorial you can try them by yourself. After running the command it's possible to stop this Microservice and MySQL container.
User Command will insert an entity into MySQL. Debezium will capture the changes and send events to kafka.

When the new entity is added, start the Denormalizer Microservice and ElasticSearch.
The Denormalizer will process the events received from Kafka and apply changes in ElasticSearch.
```bash
docker-compose up --build -d user-denormalizer elasticsearch
```

<b>Note: </b> It's possible also to stop MySQL and User Command if you don't plan to test some other operations
```bash
docker-compose stop user-command mysql
```

Check if the events were successfully processed by reading the logs of the Denormalizer. You will find the message
EventDispatcher.handleEvents took : x milliseconds

To verify that everything is working start the Query Microservice and send a request to retrieve the entities.
Use the commands
```bash
docker-compose up --build -d user-query
sh test_query.sh
```
 <b>Note: </b> It's possible also to stop Denormalizer
```bash
docker-compose stop user-denormalizer
``` 

You can acheive a realtime indexing and denormalizing by running all the containers in the same time and launching the same test commands in the tutorial for example
```bash
sh test_command.sh
sleep 3 # the process takes usually 2-3 seconds
sh test_query.sh
```
