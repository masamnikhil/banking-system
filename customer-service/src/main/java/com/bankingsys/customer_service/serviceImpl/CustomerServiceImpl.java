package com.bankingsys.customer_service.serviceImpl;

import com.bankingsys.customer_service.dto.CustomerRequestDto;
import com.bankingsys.customer_service.entity.Customer;
import com.bankingsys.customer_service.exceptionhandler.CustomerAlreadyExistsException;
import com.bankingsys.customer_service.repository.CustomerRepository;
import com.bankingsys.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public boolean registerCustomer(CustomerRequestDto customer) {

        boolean existsByEmail = customerRepository.existsByEmail(customer.getEmail());
        boolean existsByPhone = customerRepository.existsByPhoneNumber(customer.getPhoneNumber());

        if(existsByEmail)
            throw new CustomerAlreadyExistsException("customer already exists with " + customer.getEmail());
        if(existsByPhone)
            throw new CustomerAlreadyExistsException("customer already exists with " + customer.getEmail());

        try{
            Customer customer1 = Customer.builder().userId(customer.getUserId()).email(customer.getEmail()).firstName(customer.getFirstName())
                    .lastName(customer.getLastName()).phoneNumber(customer.getPhoneNumber()).dateOfBirth(customer.getDateOfBirth()).build();
            customerRepository.save(customer1);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
