package com.apidynamics.test.server_demo.controller;

import com.apidynamics.test.server_demo.service.AdaptiveAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/adaptiveAuthentication")
public class AdaptiveAuthenticationController {

    private final AdaptiveAuthenticationService adaptiveAuthenticationService;

    @Autowired
    public AdaptiveAuthenticationController(AdaptiveAuthenticationService adaptiveAuthenticationService) {
        this.adaptiveAuthenticationService = adaptiveAuthenticationService;
    }

    @GetMapping("/generateClientTotp")
    public ResponseEntity<Map<String, Object>> generate(@RequestHeader("X-API-Dynamics-Client-Id") String clientId, @RequestParam String tid) {
        Pair<HttpStatusCode, Map<String, Object>> result = adaptiveAuthenticationService.generateApiClientTotp(clientId, tid);
        HttpStatusCode httpStatusCode = result.getFirst();
        Map<String, Object> responseBody = result.getSecond();
        return new ResponseEntity<>(responseBody, httpStatusCode);
    }

    @GetMapping("/validateClientTotp")
    public ResponseEntity<Map<String, Object>> validate(@RequestParam String tid, @RequestParam String totp) {
        Pair<HttpStatusCode, Map<String, Object>> result = adaptiveAuthenticationService.validateApiClientTotp(tid, totp);
        HttpStatusCode httpStatusCode = result.getFirst();
        Map<String, Object> responseBody = result.getSecond();
        return new ResponseEntity<>(responseBody, httpStatusCode);
    }
}
