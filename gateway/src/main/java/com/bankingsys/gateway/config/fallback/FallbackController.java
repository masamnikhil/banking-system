package com.bankingsys.gateway.config.fallback;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/service")
    public ResponseEntity<String> accountFallback() {
        return ResponseEntity.status(503)
                .body("Service Unavailable, please try again later");
    }

}
