package com.bbc.count.me.up.kafka;

import com.bbc.count.me.up.model.VoteDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class VoteDtoDeserializer implements Deserializer<VoteDto> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public VoteDto deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, VoteDto.class);
        } catch (IOException e) {
            throw new RuntimeException("could not deserialize message from kafka", e);
        }
    }

    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {}

}
