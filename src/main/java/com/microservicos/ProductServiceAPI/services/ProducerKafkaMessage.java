package com.microservicos.ProductServiceAPI.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicos.ProductServiceAPI.dtos.ProductUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerKafkaMessage {
    private static final String TOPIC = "product-user-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendProductUser(ProductUserDTO dto) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dto);
        kafkaTemplate.send(TOPIC, json);
    }
}