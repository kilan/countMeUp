package com.bbc.count.me.up.service;

import java.util.concurrent.atomic.AtomicInteger;

public class VotePermit {

    private final AtomicInteger counter = new AtomicInteger();
    private int permittedVotes;

    public VotePermit(int permits) {
        this.permittedVotes = permits;
    }

    public boolean acquireVotePermission() {
        int count = counter.getAndUpdate(x -> Math.min(x + 1, permittedVotes));
        return count < permittedVotes;
    }
}
