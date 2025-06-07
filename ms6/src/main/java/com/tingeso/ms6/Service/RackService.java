package com.tingeso.ms6.Service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RackService {

    private final RestTemplate restTemplate;

    public RackService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, String>> getAllReservesFromMs5() {
        String url = "http://localhost:8005/api/reservas/"; // Cambia si tu endpoint real es otro

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> rawList = response.getBody();

        if (rawList == null) return new ArrayList<>();

        return rawList.stream().map(obj -> {
            Map<String, String> newMap = new HashMap<>();
            obj.forEach((key, value) -> {
                newMap.put(key, value != null ? value.toString() : "");
            });
            return newMap;
        }).collect(Collectors.toList());
    }
}
