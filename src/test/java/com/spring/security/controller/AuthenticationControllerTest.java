package com.spring.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.spring.security.auth.AuthenticationController;
import com.spring.security.auth.AuthenticationRequest;
import com.spring.security.auth.AuthenticationResponse;
import com.spring.security.auth.AuthenticationService;
import com.spring.security.auth.RegisterRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    private AuthenticationService authenticationService;
    private AuthenticationController controller;

    @BeforeEach
    void setUp() {
        authenticationService = mock(AuthenticationService.class); 
        controller = new AuthenticationController(authenticationService); 
    }

    @Test
    void testRegister_ShouldReturnResponseEntity() {
        RegisterRequest mockRequest = new RegisterRequest();
        AuthenticationResponse mockResponse = new AuthenticationResponse();

        when(authenticationService.register(mockRequest)).thenReturn(mockResponse);

        ResponseEntity<AuthenticationResponse> response = controller.register(mockRequest);

        assertEquals(mockResponse, response.getBody());
        verify(authenticationService, times(1)).register(mockRequest);
    }

    @Test
    void testAuthenticate_ShouldReturnResponseEntity() {
        AuthenticationRequest mockRequest = new AuthenticationRequest();
        AuthenticationResponse mockResponse = new AuthenticationResponse();

        when(authenticationService.authenticate(mockRequest)).thenReturn(mockResponse);

        ResponseEntity<AuthenticationResponse> response = controller.authenticate(mockRequest);

        assertEquals(mockResponse, response.getBody());
        verify(authenticationService, times(1)).authenticate(mockRequest);
    }

    @Test
    void testRefreshToken_ShouldCallServiceMethod() throws IOException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        controller.refreshToken(mockRequest, mockResponse);

        verify(authenticationService, times(1)).refreshToken(mockRequest, mockResponse);
    }
}
