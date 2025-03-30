package com.spring.security.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService testJwtService;

    @BeforeEach
    void setUp() {
        // ✅ Generate a valid 256-bit key
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        long jwtExpiration = 1000 * 60 * 10;       // 10 minutes
        long refreshExpiration = 1000 * 60 * 60;   // 1 hour

        testJwtService = new TestableJwtService(encodedKey, jwtExpiration, refreshExpiration);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        UserDetails user = User.withUsername("testuser").password("pass").roles("USER").build();
        String token = testJwtService.generateToken(user);

        assertNotNull(token);
        assertEquals("testuser", testJwtService.extractUsername(token));
        assertTrue(testJwtService.isTokenValid(token, user));
    }

    @Test
    void shouldGenerateRefreshToken() {
        UserDetails user = User.withUsername("refreshuser").password("pass").roles("USER").build();
        String token = testJwtService.generateRefreshToken(user);

        assertNotNull(token);
        assertEquals("refreshuser", testJwtService.extractUsername(token));
    }

    // ✅ Inner subclass to inject values without reflection
    static class TestableJwtService extends JwtService {
        public TestableJwtService(String key, long jwtExp, long refreshExp) {
            super(); // Default constructor required
            try {
                set("secretKey", key);
                set("jwtExpiration", jwtExp);
                set("refreshExpiration", refreshExp);
            } catch (Exception e) {
                throw new RuntimeException("Failed to inject test values", e);
            }
        }

        private void set(String fieldName, Object value) throws Exception {
            var field = JwtService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, value);
        }
    }
}
