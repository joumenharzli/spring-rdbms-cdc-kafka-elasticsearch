FROM confluentinc/cp-kafka-connect:latest

MAINTAINER Joumen Ali HARZLI

WORKDIR /

RUN wget https://repo1.maven.org/maven2/io/debezium/debezium-connector-mysql/0.3.0/debezium-connector-mysql-0.3.0-plugin.tar.gz -O /tmp/debezium-connector-mysql.tar.gz
RUN mkdir -p /usr/share/java/debezium-connector-mysql
RUN tar -xvzf /tmp/debezium-connector-mysql.tar.gz --directory /usr/share/java/debezium-connector-mysql
RUN rm /tmp/debezium-connector-mysql.tar.gz
