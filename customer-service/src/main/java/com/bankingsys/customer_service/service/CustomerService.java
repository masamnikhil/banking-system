package com.bankingsys.customer_service.service;

import com.bankingsys.customer_service.dto.CustomerRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    boolean registerCustomer(CustomerRequestDto customer);
}
