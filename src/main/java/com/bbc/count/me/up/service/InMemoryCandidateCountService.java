package com.bbc.count.me.up.service;

import com.bbc.count.me.up.model.VoteDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryCandidateCountService implements CandidateCountService {

    private final Map<String, AtomicLong> candidateVotes = new ConcurrentHashMap<>();
    private final Map<String, VotePermit> userVoteCounter = new ConcurrentHashMap<>();

    private final int permittedVotes;

    public InMemoryCandidateCountService(int permittedVotes) {
        this.permittedVotes = permittedVotes;
    }

    @Override
    public void addVote(VoteDto voteDto) {
        VotePermit votePermit = userVoteCounter.computeIfAbsent(voteDto.getVoterId(), s -> new VotePermit(permittedVotes));
        if(votePermit.acquireVotePermission()){
            candidateVotes.computeIfAbsent(voteDto.getCandidateId(), s -> new AtomicLong()).getAndIncrement();
        }
    }

    @Override
    public Map<String, Long> getResults() {
        return candidateVotes.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().longValue()));
    }

}
