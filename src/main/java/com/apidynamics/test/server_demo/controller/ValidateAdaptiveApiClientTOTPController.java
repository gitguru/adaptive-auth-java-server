package com.apidynamics.test.server_demo.controller;

import com.apidynamics.test.server_demo.service.AdaptiveAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ValidateAdaptiveApiClientTOTPController {

    private final AdaptiveAuthenticationService adaptiveAuthenticationService;

    @Autowired
    public ValidateAdaptiveApiClientTOTPController(AdaptiveAuthenticationService adaptiveAuthenticationService) {
        this.adaptiveAuthenticationService = adaptiveAuthenticationService;
    }

    @GetMapping("/validateAdaptiveClientTotp")
    public ResponseEntity<Map<String, Object>> validate(@RequestParam String tid, @RequestParam String totp) {
        Pair<HttpStatusCode, Map<String, Object>> result = adaptiveAuthenticationService.validateAdaptiveApiClientTotp(tid, totp);
        HttpStatusCode httpStatusCode = result.getFirst();
        Map<String, Object> responseBody = result.getSecond();
        return new ResponseEntity<>(responseBody, httpStatusCode);
    }
}
