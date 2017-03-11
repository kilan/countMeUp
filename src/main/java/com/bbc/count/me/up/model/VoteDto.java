package com.bbc.count.me.up.model;

public class VoteDto {

    private final String voterId;
    private final String candidateId;

    public VoteDto(String voterId, String candidateId) {
        this.voterId = voterId;
        this.candidateId = candidateId;
    }

    public String getVoterId() {
        return voterId;
    }

    public String getCandidateId() {
        return candidateId;
    }

}
