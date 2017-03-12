package com.bbc.count.me.up.kafka;

import com.bbc.count.me.up.model.VoteDto;
import com.bbc.count.me.up.service.CandidateCountService;
import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KafkaFetcherTest {

    private final MockConsumer<String, VoteDto> consumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
    private final CandidateCountService candidateCountService = Mockito.mock(CandidateCountService.class);
    private final KafkaFetcher kafkaFetcher = new KafkaFetcher(consumer, candidateCountService);

    private static final String TOPIC = "testTopic";

    private static final List<VoteDto> VOTE_DTOS = Arrays.asList(new VoteDto("voterId-1", "candidateId-1"),
            new VoteDto("voterId-2", "candidateId-2"),
            new VoteDto("voterId-3", "candidateId-3"));

    @Test(timeout = 10000)
    public void testKafkaFetcherForwardsMessagesAndClosesConsumer() throws InterruptedException {
        consumer.assign(Collections.singletonList(new TopicPartition(TOPIC, 0)));
        consumer.updateBeginningOffsets(ImmutableMap.of(new TopicPartition(TOPIC, 0), 0L));

        Thread thread = new Thread(kafkaFetcher);
        thread.start();

        sendMessages(VOTE_DTOS);
        while (!thread.isInterrupted()) {
            thread.interrupt();
        }

        Thread.sleep(5000);

        Assert.assertTrue(consumer.closed());
        VOTE_DTOS.forEach(voteDto -> Mockito.verify(candidateCountService).addVote(voteDto));

    }

    private void sendMessages(List<VoteDto> voteDtos) {
        for (int i = 0; i < voteDtos.size(); i++) {
            VoteDto voteDto = voteDtos.get(i);
            consumer.addRecord(new ConsumerRecord<>(TOPIC, 0, i, voteDto.getCandidateId(), voteDto));
        }
    }

}