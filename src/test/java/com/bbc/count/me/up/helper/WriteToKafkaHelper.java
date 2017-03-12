package com.bbc.count.me.up.helper;

import com.bbc.count.me.up.kafka.VoteDtoSerializer;
import com.bbc.count.me.up.model.VoteDto;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;

public class WriteToKafkaHelper {

    private static final String topic = "COUNT_ME_UP_TOPIC";
    private static final String bootstrapServers = "localhost:9092";

    private static final Logger LOG = LoggerFactory.getLogger(WriteToKafkaHelper.class);

    private static final KafkaProducer<String, VoteDto> producer = getProducer();

    @Test
    @Ignore("writes 10m votes by unique voters to the kafka topic, distributed as the scenario in the word document.")
    public void writeTenMillionMessagesToKafka() throws InterruptedException {

        int totalVotes = 10_000_000;

        LOG.info("generating messages, size={}", totalVotes);
        Map<String, Integer> candidateAndDistribution = ImmutableMap.of(
                "candidateId-1", 5,
                "candidateId-2", 10,
                "candidateId-3", 20,
                "candidateId-4", 25,
                "candidateId-5", 40);

        LOG.info("starting producer");

        candidateAndDistribution.forEach((c, d) -> IntStream
                .range(0, (int) (totalVotes * (d / 100D)))
                .mapToObj(v -> new VoteDto(c + "-" + v, c))
                .forEach(this::writeToKafka)
        );

        LOG.info("flushing");
        producer.flush();
        LOG.info("stopping producer");
        producer.close();
    }

    private void writeToKafka(VoteDto voteDto) {
        producer.send(new ProducerRecord<>(topic, voteDto.getVoterId(), voteDto));
    }

    private static Properties kafkaProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", "countMeUp-consumer-group");
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", VoteDtoSerializer.class);
        props.put("linger.ms", 5 * 10);
        props.put("batch.size", 65536 * 10);
        return props;
    }

    private static KafkaProducer<String, VoteDto> getProducer() {
        return new KafkaProducer<>(kafkaProperties());
    }


}
