package com.microservicos.ProductServiceAPI.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import com.microservicos.ProductServiceAPI.models.UserClient;

@Service
public class UserClientService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.api.url:http://localhost:8080}")
    private String userApiUrl;

    public UserClient getUserById(Long userId) {
        String url = userApiUrl + "/users/" + userId;
        return restTemplate.getForObject(url, UserClient.class);
    }
}