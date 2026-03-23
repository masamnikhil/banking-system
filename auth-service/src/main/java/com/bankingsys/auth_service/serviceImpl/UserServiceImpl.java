package com.bankingsys.auth_service.serviceImpl;

import com.bankingsys.auth_service.dto.RegisterRequest;
import com.bankingsys.auth_service.dto.UserRequestEvent;
import com.bankingsys.auth_service.entity.Role;
import com.bankingsys.auth_service.entity.User;
import com.bankingsys.auth_service.repository.UserRepository;
import com.bankingsys.auth_service.service.UserService;
import com.bankingsys.auth_service.dto.LoginRequest;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public  Map<String, String> register(RegisterRequest registerRequest) {
        Optional<User> user = userRepository.findByUsername(registerRequest.getUsername());

        if(user.isPresent())
            throw new EntityExistsException("username already exists");

        User saveUser = User.builder().username(registerRequest.getUsername()).password(registerRequest.getPassword())
                .role(Role.ROLE_CUSTOMER).enabled(true).build();

        User savedUser = userRepository.save(saveUser);
        UserRequestEvent userRequestEvent = UserRequestEvent.builder().email(registerRequest.getEmail())
                        .phoneNumber(registerRequest.getPhoneNumber()).firstName(registerRequest.getFirstName())
                        .lastName(registerRequest.getLastName()).dateOfBirth(registerRequest.getDateOfBirth()).build();
        kafkaTemplate.send("user_created", userRequestEvent);
        return null;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        return "";
    }
}
