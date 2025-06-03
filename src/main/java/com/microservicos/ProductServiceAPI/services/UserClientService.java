package com.microservicos.ProductServiceAPI.services;

import com.microservicos.ProductServiceAPI.models.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserClientService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.api.url:http://localhost:8080}")
    private String userApiUrl;

    @Autowired
    private RedisTemplate<String, UserClient> redisTemplate;

    public UserClient getUserById(Long userId) {
        String url = userApiUrl + "/users/" + userId;
        return restTemplate.getForObject(url, UserClient.class);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "getUsersByIdsFallback")
    public Map<Long, UserClient> getUsersByIds(List<Long> ids) {
        // Busca do Redis
        List<String> redisKeys = ids.stream().map(id -> "user:" + id).collect(Collectors.toList());
        List<UserClient> cachedUsers = redisTemplate.opsForValue().multiGet(redisKeys);
        Map<Long, UserClient> result = new HashMap<>();
        Set<Long> missingIds = new HashSet<>();
        for (int i = 0; i < ids.size(); i++) {
            UserClient user = cachedUsers.get(i);
            if (user != null) {
                result.put(ids.get(i), user);
            } else {
                missingIds.add(ids.get(i));
            }
        }
        // Busca os que faltam na API
        if (!missingIds.isEmpty()) {
            String idsParam = missingIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            String url = userApiUrl + "/users/by-ids?ids=" + idsParam;
            UserClient[] usersFromApi = restTemplate.getForObject(url, UserClient[].class);
            if (usersFromApi != null) {
                for (UserClient user : usersFromApi) {
                    result.put(user.getId(), user);
                    redisTemplate.opsForValue().set("user:" + user.getId(), user);
                }
            }
        }
        return result;
    }

    public Map<Long, UserClient> getUsersByIdsFallback(List<Long> ids, Throwable t) {
        // Fallback: retorna apenas o que estiver no cache
        List<String> redisKeys = ids.stream().map(id -> "user:" + id).collect(Collectors.toList());
        List<UserClient> cachedUsers = redisTemplate.opsForValue().multiGet(redisKeys);
        Map<Long, UserClient> result = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            UserClient user = cachedUsers.get(i);
            if (user != null) {
                result.put(ids.get(i), user);
            }
        }
        return result;
    }

    public String getUserApiUrl() {
        return userApiUrl;
    }
}