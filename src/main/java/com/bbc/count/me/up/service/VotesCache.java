package com.bbc.count.me.up.service;

import com.bbc.count.me.up.model.CandidateResults;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class VotesCache {

    private static final Logger LOG = getLogger(VotesCache.class);

    private final CandidateCountService candidateCountService;
    private volatile List<CandidateResults> cachedVotes;


    public VotesCache(CandidateCountService candidateCountService) {
        this.candidateCountService = candidateCountService;
    }

    public List<CandidateResults> getVotes() {
        return cachedVotes;
    }

    @Scheduled(fixedDelay = 100)
    private void reloadCache() {
        LOG.debug("caching");
        cachedVotes = candidateCountService.getResults().entrySet().stream()
                .map(e -> new CandidateResults(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

}
