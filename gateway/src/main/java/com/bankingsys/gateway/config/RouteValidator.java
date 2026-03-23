package com.bankingsys.gateway.config;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/refresh"
    );

    public Predicate<String> isSecured =
            path -> openApiEndpoints
                    .stream()
                    .noneMatch(path::contains);
}
