package com.spring.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.spring.security.booking.Room;
import com.spring.security.booking.RoomRepository;
import com.spring.security.booking.RoomService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room availableRoom;
    private Room bookedRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        availableRoom = new Room();
        availableRoom.setId(1L);
        availableRoom.setBooked(false);

        bookedRoom = new Room();
        bookedRoom.setId(2L);
        bookedRoom.setBooked(true);
        bookedRoom.setBookedBy(10L);
    }

    @Test
    void testGetAllRooms_ReturnsList() {
        List<Room> rooms = List.of(availableRoom, bookedRoom);
        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.getAllRooms();

        assertEquals(2, result.size());
        verify(roomRepository).findAll();
    }

    @Test
    void testBookRoom_Success() throws Exception {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(availableRoom));
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Room result = roomService.bookRoom(1L, 100L);

        assertTrue(result.isBooked());
        assertEquals(100L, result.getBookedBy());
        verify(roomRepository).save(result);
    }

    @Test
    void testBookRoom_AlreadyBooked_ThrowsException() {
        when(roomRepository.findById(2L)).thenReturn(Optional.of(bookedRoom));

        Exception ex = assertThrows(Exception.class, () -> roomService.bookRoom(2L, 200L));
        assertEquals("Room is already booked.", ex.getMessage());
    }

    @Test
    void testBookRoom_NotFound_ThrowsException() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> roomService.bookRoom(999L, 100L));
        assertEquals("Room not found.", ex.getMessage());
    }

    @Test
    void testUnbookRoom_Success() throws Exception {
        when(roomRepository.findById(2L)).thenReturn(Optional.of(bookedRoom));
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Room result = roomService.unbookRoom(2L, 10L);

        assertFalse(result.isBooked());
        assertNull(result.getBookedBy());
        verify(roomRepository).save(result);
    }

    @Test
    void testUnbookRoom_WrongUser_ThrowsException() {
        when(roomRepository.findById(2L)).thenReturn(Optional.of(bookedRoom));

        Exception ex = assertThrows(Exception.class, () -> roomService.unbookRoom(2L, 999L));
        assertEquals("You cannot unbook this room.", ex.getMessage());
    }

    @Test
    void testUnbookRoom_NotFound_ThrowsException() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> roomService.unbookRoom(999L, 100L));
        assertEquals("Room not found.", ex.getMessage());
    }
}
