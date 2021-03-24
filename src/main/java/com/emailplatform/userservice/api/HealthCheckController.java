package com.emailplatform.userservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HealthCheckController {

    @GetMapping("/health-check/test")
    public ResponseEntity<HttpStatus> healthCheck() {
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
