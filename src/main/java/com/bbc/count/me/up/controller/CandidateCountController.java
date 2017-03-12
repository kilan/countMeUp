package com.bbc.count.me.up.controller;

import com.bbc.count.me.up.model.CandidateResults;
import com.bbc.count.me.up.service.VotesCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CandidateCountController {

    private final VotesCache votesCache;

    @Autowired
    public CandidateCountController(VotesCache votesCache) {
        this.votesCache = votesCache;
    }

    @RequestMapping("votes")
    public List<CandidateResults> candidateCount() {
        return votesCache.getVotes();
    }

}
