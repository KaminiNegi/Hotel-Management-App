package com.spring.security.token;

import com.spring.security.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TokenRepositoryTest {

    @Mock
    private TokenRepository tokenRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1); // Integer ID
    }

    @Test
    void testFindAllValidTokenByUser_shouldReturnList() {
        Token token1 = Token.builder()
                .id(1)
                .token("token123")
                .expired(false)
                .revoked(false)
                .user(user)
                .build();

        Token token2 = Token.builder()
                .id(2)
                .token("token456")
                .expired(false)
                .revoked(false)
                .user(user)
                .build();

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(List.of(token1, token2));

        List<Token> result = tokenRepository.findAllValidTokenByUser(user.getId());

        assertEquals(2, result.size());
        assertFalse(result.get(0).isExpired());
        assertFalse(result.get(1).isRevoked());
    }

    @Test
    void testFindByToken_shouldReturnToken() {
        Token token = Token.builder()
                .id(1)
                .token("abc123")
                .expired(false)
                .revoked(false)
                .user(user)
                .build();

        when(tokenRepository.findByToken("abc123")).thenReturn(Optional.of(token));

        Optional<Token> result = tokenRepository.findByToken("abc123");

        assertTrue(result.isPresent());
        assertEquals("abc123", result.get().getToken());
    }
}
