package com.bbc.count.me.up.service;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

public class VotesCache {

    private final CandidateCountService candidateCountService;
    private volatile Map<String, Long> cachedVotes;


    public VotesCache(CandidateCountService candidateCountService) {
        this.candidateCountService = candidateCountService;
    }

    public Map<String, Long> getVotes() {
        return cachedVotes;
    }

    @Scheduled(fixedDelay = 100)
    private void reloadCache() {
        cachedVotes = candidateCountService.getResults();
    }

}
