package com.bbc.count.me.up.controller;

import com.bbc.count.me.up.service.CandidateCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class CandidateCountController {

    private final CandidateCountService candidateCountService;

    @Autowired
    public CandidateCountController(CandidateCountService candidateCountService) {
        this.candidateCountService = candidateCountService;
    }

    @RequestMapping("votes")
    public Map<String, Long> candidateCount() {
        return candidateCountService.getResults();
    }

}
