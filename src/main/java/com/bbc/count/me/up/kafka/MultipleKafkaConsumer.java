package com.bbc.count.me.up.kafka;

import com.bbc.count.me.up.model.VoteDto;
import com.bbc.count.me.up.service.CandidateCountService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class MultipleKafkaConsumer {

    private final Supplier<Consumer<String, VoteDto>> kafkaConsumerSupplier;
    private final CandidateCountService candidateCountService;
    private final String topic;
    private ExecutorService pool;

    public MultipleKafkaConsumer(Supplier<Consumer<String, VoteDto>> kafkaConsumerSupplier,
                                 CandidateCountService candidateCountService,
                                 String topic) {

        this.kafkaConsumerSupplier = kafkaConsumerSupplier;
        this.candidateCountService = candidateCountService;
        this.topic = topic;
    }

    @PostConstruct
    public void start() {
        int numberOfConsumers = getNumberOfPartitions();
        pool = Executors.newFixedThreadPool(numberOfConsumers);
        for (int i = 0; i < numberOfConsumers; i++) {
            Consumer<String, VoteDto> kafkaConsumer = kafkaConsumerSupplier.get();

            kafkaConsumer.assign(Collections.singletonList(new TopicPartition(topic, i)));
            kafkaConsumer.seekToBeginning(Collections.singletonList(new TopicPartition(topic, i)));

            pool.submit(new KafkaFetcher(kafkaConsumer, candidateCountService));
        }
    }

    @PreDestroy
    public void stop() {
        //https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
        if (pool != null) {
            pool.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    pool.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                        System.err.println("Pool did not terminate");
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                pool.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }

    private int getNumberOfPartitions() {
        Consumer consumer = kafkaConsumerSupplier.get();
        consumer.subscribe(Collections.singletonList(topic));
        int partitions = consumer.partitionsFor(topic).size();

        consumer.close();

        return partitions;
    }
}
