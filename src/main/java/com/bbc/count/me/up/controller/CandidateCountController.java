package com.bbc.count.me.up.controller;

import com.bbc.count.me.up.service.VotesCache;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CandidateCountController {

    private final VotesCache votesCache;

    @Autowired
    public CandidateCountController(VotesCache votesCache) {
        this.votesCache = votesCache;
    }

    @RequestMapping("votes")
    public Map<String, Long> candidateCount() {
        return votesCache.getVotes();
    }

}
