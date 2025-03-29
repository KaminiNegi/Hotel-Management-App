package com.spring.security.service;

import com.spring.security.auth.AuthenticationRequest;
import com.spring.security.auth.AuthenticationResponse;
import com.spring.security.auth.AuthenticationService;
import com.spring.security.auth.RegisterRequest;
import com.spring.security.config.JwtService;
import com.spring.security.token.Token;
import com.spring.security.token.TokenRepository;
import com.spring.security.users.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
 class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authRequest;
    private User user;

    @BeforeEach
    void setup() {
        registerRequest = new RegisterRequest("John", "Doe", "john@example.com", "password");
        authRequest = new AuthenticationRequest("john@example.com", "password");

        user = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("encodedPass")
                .roles(Set.of(new Role(1, RoleType.USER)))
                .createdAt(new Date())
                .build();
    }

    @Test
    void testRegister_NewUser_Success() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleType.USER)).thenReturn(Optional.of(new Role(1, RoleType.USER)));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh-token");

        AuthenticationResponse response = service.register(registerRequest);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testRegister_UserAlreadyExists_ThrowsException() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.register(registerRequest));
        assertEquals("User already exists with this email", ex.getMessage());
    }

    @Test
    void testRegister_UserRoleNotFound_ThrowsException() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleType.USER)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.register(registerRequest));
        assertEquals("Default USER role not found", ex.getMessage());
    }

    @Test
    void testAuthenticate_Success() {
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");

        List<Token> existingTokens = new ArrayList<>();
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(existingTokens);

        AuthenticationResponse response = service.authenticate(authRequest);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void testRefreshToken_InvalidHeader_NoAction() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        service.refreshToken(request, response);

        verifyNoInteractions(userRepository);
    }

    @Test
    void testRefreshToken_TokenNotValid_NoAction() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer fake-token");
        when(jwtService.extractUsername("fake-token")).thenReturn("john@example.com");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("fake-token", user)).thenReturn(false);

        service.refreshToken(request, response);
        verify(jwtService, never()).generateToken(any());
    }
}
