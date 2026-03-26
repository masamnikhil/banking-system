package com.bankingsys.auth_service.feignclient;

import com.bankingsys.auth_service.dto.CustomerRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerClient {

    @PostMapping("/customer/register")
    ResponseEntity<HttpStatus> createCustomer(@RequestBody CustomerRequestDto dto);
}
