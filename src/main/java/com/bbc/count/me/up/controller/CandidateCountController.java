package com.bbc.count.me.up.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class CandidateCountController {

    @RequestMapping("votes")
    public Map<String, Long> candidateCount() {
        return Collections.emptyMap();
    }

}
