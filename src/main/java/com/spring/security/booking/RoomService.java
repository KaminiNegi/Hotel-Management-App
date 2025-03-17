package com.spring.security.booking;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room bookRoom(Long roomId, Long userId) throws Exception {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (!room.isBooked()) {
                room.setBooked(true);
                room.setBookedBy(userId);
                return roomRepository.save(room);
            } else {
                throw new Exception("Room is already booked.");
            }
        }
        throw new Exception("Room not found.");
    }

    public Room unbookRoom(Long roomId, Long userId) throws Exception {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (room.isBooked() && room.getBookedBy().equals(userId)) {
                room.setBooked(false);
                room.setBookedBy(null);
                return roomRepository.save(room);
            } else {
                throw new Exception("You cannot unbook this room.");
            }
        }
        throw new Exception("Room not found.");
    }
}
