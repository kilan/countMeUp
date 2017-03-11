package com.bbc.count.me.up.service;

import org.junit.Assert;
import org.junit.Test;

public class VotePermitTest {

    @Test
    public void testVotePermitter() {
        int permits = 3;
        VotePermit votePermit = new VotePermit(permits);

        for (int i = 0; i < permits; i++) {
            Assert.assertTrue(votePermit.acquireVotePermission());
        }

        Assert.assertFalse(votePermit.acquireVotePermission());
    }

}