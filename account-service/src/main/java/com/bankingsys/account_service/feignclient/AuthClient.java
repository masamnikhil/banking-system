package com.bankingsys.account_service.feignclient;

import com.bankingsys.account_service.dto.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTH_SERVICE", configuration = FeignClientConfig.class)
public interface AuthClient {

    @PostMapping("/auth/verify")
    ResponseEntity<HttpStatus> verifyProfilePassword(@RequestBody UserRequest request);
}
