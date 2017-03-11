package com.bbc.count.me.up.model;

public class VoteDto {

    private String voterId;
    private String candidateId;

    public VoteDto() {

    }

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

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }
}
