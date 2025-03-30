package com.spring.security.booking;

import com.spring.security.users.User;
import com.spring.security.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.webjars.NotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class RoomControllerTest {

    @InjectMocks
    private RoomController roomController;

    @Mock
    private RoomService roomService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(100); // Integer
    }

    @Test
    void getRooms_shouldReturnAllRooms() {
        List<Room> rooms = List.of(new Room(1L, 101, false, null));
        when(roomService.getAllRooms()).thenReturn(rooms);

        List<Room> result = roomController.getRooms();

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getRoomNumber());
        verify(roomService).getAllRooms();
    }

    @Test
    void bookRoom_shouldSucceed() throws Exception {
        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Room room = new Room(1L, 101, true, 100L);
        when(roomService.bookRoom(1L, 100L)).thenReturn(room);

        Map<String, Long> request = new HashMap<>();
        request.put("roomId", 1L);

        Room result = roomController.bookRoom(request, authentication);

        assertEquals(100L, result.getBookedBy());
        assertTrue(result.isBooked());
        verify(roomService).bookRoom(1L, 100L);
    }

    @Test
    void bookRoom_shouldThrowWhenUserNotFound() {
        when(authentication.getName()).thenReturn("unknown@example.com");
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Map<String, Long> request = new HashMap<>();
        request.put("roomId", 1L);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            roomController.bookRoom(request, authentication);
        });

        assertEquals("User not found!", ex.getMessage());
    }

    @Test
    void unbookRoom_shouldSucceed() throws Exception {
        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Room room = new Room(1L, 101, false, null);
        when(roomService.unbookRoom(1L, 100L)).thenReturn(room);

        Map<String, Long> request = new HashMap<>();
        request.put("roomId", 1L);

        Room result = roomController.unbookRoom(request, authentication);

        assertEquals(101, result.getRoomNumber());
        assertFalse(result.isBooked());
        verify(roomService).unbookRoom(1L, 100L);
    }

    @Test
    void unbookRoom_shouldThrowWhenUserNotFound() {
        when(authentication.getName()).thenReturn("missing@example.com");
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        Map<String, Long> request = new HashMap<>();
        request.put("roomId", 1L);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            roomController.unbookRoom(request, authentication);
        });

        assertEquals("User not found!", ex.getMessage());
    }
}
