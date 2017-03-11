# countMeUp# countMeUp

./zookeeper-server-start.sh ../config/zookeeper.properties
./kafka-server-start.sh ../config/server.properties

./kafka-topics.sh --create --topic BBC.TOPIC.VOTES.TEST.002 --partitions 15  --zookeeper localhost:2181 --replication-factor 1

./kafka-console-consumer.sh --topic BBC.TOPIC.VOTES_TEST_001 --zookeeper localhost:2181 --from-beginning