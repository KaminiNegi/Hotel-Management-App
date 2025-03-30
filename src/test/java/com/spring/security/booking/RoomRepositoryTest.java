package com.spring.security.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class RoomRepositoryMockTest {

    @Mock
    private RoomRepository roomRepository;

    @Test
    void testFindByIsBooked_ShouldReturnBookedRooms() {
        Room room = new Room(1L, 101, true, 100L);
        when(roomRepository.findByIsBooked(true)).thenReturn(List.of(room));

        List<Room> result = roomRepository.findByIsBooked(true);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isBooked());
        verify(roomRepository).findByIsBooked(true);
    }

    @Test
    void testFindByIsBooked_ShouldReturnEmptyList() {
        when(roomRepository.findByIsBooked(true)).thenReturn(List.of());

        List<Room> result = roomRepository.findByIsBooked(true);

        assertTrue(result.isEmpty());
        verify(roomRepository).findByIsBooked(true);
    }
}
