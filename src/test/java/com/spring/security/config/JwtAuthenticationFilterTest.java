package com.spring.security.config;

import com.spring.security.token.Token;
import com.spring.security.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService, tokenRepository);
    }

    @Test
    void shouldSkipAuthEndpoint() throws Exception {
        when(request.getServletPath()).thenReturn("/api/v1/auth/login");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid() throws Exception {
        String token = "valid.jwt.token";

        when(request.getServletPath()).thenReturn("/api/v1/protected");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn("testuser");

        UserDetails user = User.withUsername("testuser").password("pass").roles("USER").build();
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(user);

        Token storedToken = new Token();
        storedToken.setExpired(false);
        storedToken.setRevoked(false);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(storedToken));
        when(jwtService.isTokenValid(token, user)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
