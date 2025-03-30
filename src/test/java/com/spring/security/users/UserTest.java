package com.spring.security.users;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserBuilderAndFields() {
        Role role = new Role(1, RoleType.ADMIN);
        Set<Role> roles = new HashSet<>(Collections.singletonList(role));

        User user = User.builder()
                .id(1)
                .firstname("Alice")
                .lastname("Smith")
                .email("alice@example.com")
                .password("pass")
                .roles(roles)
                .build();

        assertEquals("Alice", user.getFirstname());
        assertEquals("Smith", user.getLastname());
        assertEquals("alice@example.com", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());

        assertEquals(1, user.getAuthorities().size());
    }
}
