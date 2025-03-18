package com.apidynamics.test.server_demo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class ApiDynamicsClient {
    private String clientId;
    private Map<String, String> requestHeaders;
    private String requestEndpoint;
    private String requestMethod;

    public Map<String, String> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("client_id", clientId);
        try {
            map.put("client_request_headers", objectMapper.writeValueAsString(requestHeaders));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        map.put("client_request_endpoint", requestEndpoint);
        map.put("client_request_method", requestMethod);
        return map;
    }

    public String stringify() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        try {
            map.put("client_id", clientId);
            map.put("client_request_headers", objectMapper.writeValueAsString(requestHeaders));
            map.put("client_request_endpoint", requestEndpoint);
            map.put("client_request_method", requestMethod);

            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
