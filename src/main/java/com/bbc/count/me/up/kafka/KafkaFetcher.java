package com.bbc.count.me.up.kafka;

import com.bbc.count.me.up.model.VoteDto;
import com.bbc.count.me.up.service.CandidateCountService;
import org.apache.kafka.clients.consumer.Consumer;

public class KafkaFetcher implements Runnable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(KafkaFetcher.class);

    private final Consumer<String, VoteDto> kafkaConsumer;
    private final CandidateCountService candidateCountService;

    public KafkaFetcher(Consumer<String, VoteDto> kafkaConsumer, CandidateCountService candidateCountService) {
        this.kafkaConsumer = kafkaConsumer;
        this.candidateCountService = candidateCountService;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                kafkaConsumer.poll(1000)
                        .forEach(r -> candidateCountService.addVote(r.value()));
            }
            LOG.info("shutting down");
        } catch (Exception e) {
            LOG.warn("could not consume from kafka", e);
        } finally {
            kafkaConsumer.close();
        }
    }
}
