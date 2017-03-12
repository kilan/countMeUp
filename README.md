# CountMeUp

- The application only requires Kafka as an external dependency.
-- To download Kafka go to https://kafka.apache.org/downloads.html  
-- Unzip it and cd to KAFKA_DIR/bin/  
-- start Zookeeper: `./zookeeper-server-start.sh ../config/zookeeper.properties`  
-- start Kafka server: `./kafka-server-start.sh ../config/server.properties`  
-- create a Kafka topic: `./kafka-topics.sh --create --topic COUNT_ME_UP_TOPIC --partitions 15  --zookeeper localhost:2181 --replication-factor 1`  


- build the project using maven: `mvn clean install`.  
- to start the local erver: `mvn spring-boot:start`  
