package com.bbc.count.me.up.service;

import com.bbc.count.me.up.model.VoteDto;

import java.util.Map;

public interface CandidateCountService {

    public void addVote(VoteDto voteDto);

    Map<String,Long> getResults();
}