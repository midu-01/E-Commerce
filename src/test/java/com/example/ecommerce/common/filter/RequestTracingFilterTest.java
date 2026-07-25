package com.example.ecommerce.common.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RequestTracingFilterTest {

    private final RequestTracingFilter filter = new RequestTracingFilter();

    @Test
    void shouldGenerateTraceIdIfMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getHeader("X-Trace-Id")).isNotNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldReuseExistingTraceId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String existingTraceId = "custom-trace-123";
        request.addHeader("X-Trace-Id", existingTraceId);
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getHeader("X-Trace-Id")).isEqualTo(existingTraceId);
        verify(filterChain).doFilter(request, response);
    }
}