package com.apidynamics.test.server_demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class CurrentTimeController {

	private static final String template = "It is, %s!";
	private final AtomicLong counter = new AtomicLong();

	/**
	 * An Api Client will be able to hit this method ONLY if it passes Adaptive Authentication
	 * Check AdaptiveAuthenticationFilter for more details
	 *
	 * Even if the response of this method is very simple,
	 * what is being demonstrated here is the Adaptive Authentication process.
	 *
	 * @return Current UTC time
	 */
	@GetMapping("/api/timestamp")
	public ResponseEntity<Map<String, Object>> currentTimestamp() {
		Map<String, Object> resp = new HashMap<>();
		resp.put("id", counter.incrementAndGet());
		resp.put("timestamp", new Date());
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
}
