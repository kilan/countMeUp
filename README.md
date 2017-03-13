# CountMeUp

- The application only requires Kafka as an external dependency.
-- To download Kafka go to https://kafka.apache.org/downloads.html  
-- Unzip it and cd to KAFKA_DIR/bin/  
-- start Zookeeper: `./zookeeper-server-start.sh ../config/zookeeper.properties`  
-- start Kafka server: `./kafka-server-start.sh ../config/server.properties`  
-- create a Kafka topic: `./kafka-topics.sh --create --topic COUNT_ME_UP_TOPIC --partitions 15  --zookeeper localhost:2181 --replication-factor 1`  


- build the project using maven: `mvn clean install`.  
- to start the local server: `mvn spring-boot:start`  

## Assumptions
- Application is only for a single voting event and is responsible for only counting votes.
- The data in the Kafka topic is valid. I'm assuming the source has authenticated the voter and candidate IDs.
- Application is running in a blue-green deployment so restarting the application would be possible.
- The TV presenter is happy to present a JSON to the nation as I'm not familiar with front-end development.

## Decisions
- I've used Kafka as it can store the state and is easy to parallelise consumption on the client side.  
	- On a restart the application would then restore and hold the state locally from Kafka.  
- I am caching the result as I don't want each request to reprocess the state.  

## Potential Changes
- Slow restart: If the vote count was in the hundreds of millions, then I would persist to a database periodically:  
	- the votes for each candidate.  
	- number of times a user voted.  
	- kafka offsets for each partition.  
- This would allow the application to continue to continue from where it previously restarted. However in this case Kafka would not be required and a traditional JMS queue could be used instead.  
- Kafka-streams could be potentially used as it persists state RocksDB, an embedded database.  
- Spark Streaming could be used stream from Kafka and to compute the voting result and notify the results to another service or persist to a database.  
