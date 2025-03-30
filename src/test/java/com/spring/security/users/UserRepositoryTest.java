package com.spring.security.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .firstname("Jane")
                .lastname("Doe")
                .email("jane@example.com")
                .password("secret")
                .roles(Set.of(new Role(1, RoleType.USER)))
                .build();
    }

    @Test
    void testFindByEmail_shouldReturnUser() {
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByEmail("jane@example.com");

        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getFirstname());
    }

    @Test
    void testExistsByEmail_shouldReturnTrue() {
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        assertTrue(userRepository.existsByEmail("jane@example.com"));
    }

    @Test
    void testFindAllWithRoles_shouldReturnList() {
        when(userRepository.findAllWithRoles()).thenReturn(List.of(user));

        List<User> result = userRepository.findAllWithRoles();

        assertEquals(1, result.size());
        assertEquals("jane@example.com", result.get(0).getEmail());
    }
}
