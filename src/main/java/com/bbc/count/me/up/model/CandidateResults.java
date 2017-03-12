package com.bbc.count.me.up.model;

public class CandidateResults {

    private String key;
    private Long value;

    public CandidateResults() {
    }

    public CandidateResults(String key, Long value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Long getValue() {
        return value;
    }
}
