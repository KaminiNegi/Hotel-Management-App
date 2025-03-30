package com.spring.security.config;

import com.spring.security.token.Token;
import com.spring.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class LogoutServiceTest {

    @InjectMocks
    private LogoutService logoutService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Test
    void shouldLogoutIfTokenIsValid() {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");

        Token token = new Token();
        token.setExpired(false);
        token.setRevoked(false);

        when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

        logoutService.logout(request, response, authentication);

        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
        verify(tokenRepository).save(token);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldDoNothingIfAuthHeaderMissing() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).save(any());
    }
}
