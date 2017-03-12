package com.bbc.count.me.up.service;

import com.bbc.count.me.up.model.VoteDto;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class InMemoryCandidateCountServiceTest {

    private final CandidateCountService service = new InMemoryCandidateCountService(3);

    @Test
    public void testVotesAreDistributed() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 100; j++) {
                service.addVote(new VoteDto("voterId-" + i + "-" + j, "candidateId-" + i));
            }
        }

        Map<String, Long> expectedResults = ImmutableMap.of(
                "candidateId-0", 100L,
                "candidateId-1", 100L,
                "candidateId-2", 100L,
                "candidateId-3", 100L,
                "candidateId-4", 100L);

        Map<String, Long> actualVotes = service.getResults();

        assertMapEquals(expectedResults, actualVotes);
    }

    @Test
    public void testVoterCannotVoteMoreThanThreeTimes() {
        service.addVote(new VoteDto("voterId-1", "candidateId-1"));
        service.addVote(new VoteDto("voterId-1", "candidateId-2"));
        service.addVote(new VoteDto("voterId-1", "candidateId-3"));
        service.addVote(new VoteDto("voterId-1", "candidateId-4"));

        Map<String, Long> actualVotes = service.getResults();

        Map<String, Long> expectedResults = ImmutableMap.of(
                "candidateId-1", 1L,
                "candidateId-2", 1L,
                "candidateId-3", 1L);

        assertMapEquals(expectedResults, actualVotes);
    }

    @Test
    @Ignore("slows the build")
    public void testTenMillionVotesAreComputedUnderASecond() {
        int totalVotes = 10_000_000;

        Map<String, Integer> candidateAndDistribution = ImmutableMap.of(
                "candidateId-1", 5,
                "candidateId-2", 10,
                "candidateId-3", 20,
                "candidateId-4", 25,
                "candidateId-5", 40);

        candidateAndDistribution
                .forEach((c, d) ->
                        IntStream.range(0, (int) (totalVotes * (d / 100D)))
                                .parallel()
                                .forEach(v -> service.addVote(new VoteDto(c + v, c)))
                );

        long startTime = System.currentTimeMillis();
        Map<String, Long> actualVotes = service.getResults();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        Map<String, Long> expectedResults = ImmutableMap.of(
                "candidateId-1", 500000L,
                "candidateId-2", 1000000L,
                "candidateId-3", 2000000L,
                "candidateId-4", 2500000L,
                "candidateId-5", 4000000L);

        assertMapEquals(expectedResults, actualVotes);

        Assert.assertTrue("Test Timed out, duration=" + duration, duration < TimeUnit.SECONDS.toMillis(1));
    }

    private void assertMapEquals(Map<String, Long> expectedVotes, Map<String, Long> actualVotes) {
        MapDifference<String, Long> difference = Maps.difference(expectedVotes, actualVotes);
        Assert.assertTrue("Entries differing: " + difference.entriesDiffering(), difference.areEqual());
    }

}