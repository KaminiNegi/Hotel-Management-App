package com.spring.security.controller;

import com.spring.security.auth.*;
import com.spring.security.users.Role;
import com.spring.security.users.RoleType;
import com.spring.security.users.User;
import com.spring.security.users.UserController;
import com.spring.security.users.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers_ShouldReturnUsers() throws Exception {
        User mockUser = User.builder()
                .id(1)
                .email("user@example.com")
                .roles(Set.of(Role.builder().name(RoleType.USER).build()))
                .build();

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUser));
        when(userRepository.findAllWithRoles()).thenReturn(List.of(mockUser));

        mockMvc.perform(get("/api/v1/users").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@example.com"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        User mockUser = User.builder()
                .id(1)
                .email("user@example.com")
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }
}

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void register_ShouldReturnAuthResponse() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");
        request.setRoleType(RoleType.ADMIN);
        request.setFirstname("user");
        request.setLastname("user");
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken("mock_access_token")
                .refreshToken("mock_refresh_token")
                .build();

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("mock_access_token"))
                .andExpect(jsonPath("$.refresh_token").value("mock_refresh_token"));
    }

    @Test
    void authenticate_ShouldReturnAuthResponse() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken("mock_access_token")
                .refreshToken("mock_refresh_token")
                .build();

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("mock_access_token"))
                .andExpect(jsonPath("$.refresh_token").value("mock_refresh_token"));
    }
}
