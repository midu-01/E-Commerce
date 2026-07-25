package com.example.ecommerce.common.controller;

import com.example.ecommerce.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "System", description = "System health and utility endpoints")
public class HealthController {

    @GetMapping
    @Operation(summary = "Check API health", description = "Returns the health status of the API")
    public ApiResponse<Map<String, String>> healthCheck() {
        return ApiResponse.success(Map.of("status", "UP", "service", "ecommerce-api"));
    }
}