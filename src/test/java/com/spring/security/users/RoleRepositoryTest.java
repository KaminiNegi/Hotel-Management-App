package com.spring.security.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    void testFindByName_shouldReturnRole() {
        Role role = new Role(1, RoleType.USER);

        when(roleRepository.findByName(RoleType.USER)).thenReturn(Optional.of(role));

        Optional<Role> result = roleRepository.findByName(RoleType.USER);

        assertTrue(result.isPresent());
        assertEquals(RoleType.USER, result.get().getName());
    }

    @Test
    void testFindByName_shouldReturnEmptyIfNotFound() {
        when(roleRepository.findByName(RoleType.MANAGER)).thenReturn(Optional.empty());

        Optional<Role> result = roleRepository.findByName(RoleType.MANAGER);

        assertTrue(result.isEmpty());
    }
}
