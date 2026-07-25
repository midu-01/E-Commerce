package com.example.ecommerce.common.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void shouldCreateSuccessResponse() {
        String data = "test data";
        ApiResponse<String> response = ApiResponse.success(data);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Operation successful");
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getErrors()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void shouldCreateErrorResponse() {
        String errorMessage = "Validation failed";
        List<String> errors = List.of("Field cannot be null");
        
        ApiResponse<Void> response = ApiResponse.error(errorMessage, errors);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo(errorMessage);
        assertThat(response.getData()).isNull();
        assertThat(response.getErrors()).containsExactly("Field cannot be null");
        assertThat(response.getTimestamp()).isNotNull();
    }
}