package com.apidynamics.test.server_demo.model;

import lombok.Data;

// TODO : Find a usage for this in the future
@Data
public class AdaptiveAuthValidationResult {
    private int score;
    private String decision;
}
