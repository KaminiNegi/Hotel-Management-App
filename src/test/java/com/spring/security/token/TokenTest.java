package com.spring.security.token;

import com.spring.security.users.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void shouldBuildTokenCorrectly() {
        User user = new User();
        user.setId(1);

        Token token = Token.builder()
                .id(10)
                .token("sample-token")
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();

        assertEquals(10, token.getId());
        assertEquals("sample-token", token.getToken());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
        assertEquals(1, token.getUser().getId());
    }

    @Test
    void shouldSettersAndGettersWorkCorrectly() {
        Token token = new Token();
        token.setId(5);
        token.setToken("new-token");
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(true);
        token.setExpired(true);

        User user = new User();
        user.setId(99);
        token.setUser(user);

        assertEquals(5, token.getId());
        assertEquals("new-token", token.getToken());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertTrue(token.isRevoked());
        assertTrue(token.isExpired());
        assertEquals(99, token.getUser().getId());
    }
}
