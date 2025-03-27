package com.spring.security.booking;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByIsBooked(boolean isBooked);
}