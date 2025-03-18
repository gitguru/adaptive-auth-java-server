package com.apidynamics.test.server_demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// TODO : Find a usage for this in the future
@Data
public class AdaptiveAuthResponse {
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("validation_result")
    private AdaptiveAuthValidationResult validationResult;
}
