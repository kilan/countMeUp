package com.bbc.count.me.up.config;

import com.bbc.count.me.up.kafka.MultipleKafkaConsumer;
import com.bbc.count.me.up.kafka.VoteDtoDeserializer;
import com.bbc.count.me.up.model.VoteDto;
import com.bbc.count.me.up.service.CandidateCountService;
import com.bbc.count.me.up.service.InMemoryCandidateCountService;
import com.bbc.count.me.up.service.VotesCache;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.function.Supplier;

@Configuration
public class BeanConfig {

    @Bean
    public CandidateCountService candidateCountService(@Value("${permitted.votes}") int permittedVotes) {
        return new InMemoryCandidateCountService(permittedVotes);
    }

    @Bean
    public MultipleKafkaConsumer multipleKafkaConsumer(@Value("${kafka.vote.topic}") String topic,
                                                       Supplier<Consumer<String, VoteDto>> kafkaConsumerSupplier,
                                                       CandidateCountService candidateCountService) {
        return new MultipleKafkaConsumer(kafkaConsumerSupplier, candidateCountService, topic);
    }

    @Bean
    public Supplier<Consumer<String, VoteDto>> kafkaConsumerSupplier(Properties kafkaProperties) {
        return () -> new KafkaConsumer<>(kafkaProperties);
    }

    @Bean
    public Properties kafkaProperties(@Value("${kafka.bootstrap.servers}") String bootstrapServers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", "countMeUp-consumer-group");
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", VoteDtoDeserializer.class);
        props.put("enable.auto.commit", "true");
        return props;
    }

    @Bean
    public VotesCache votesCache(CandidateCountService candidateCountService) {
        return new VotesCache(candidateCountService);
    }

}
