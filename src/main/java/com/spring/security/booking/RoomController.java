package com.spring.security.booking;


import com.spring.security.users.User;
import com.spring.security.users.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("*") // Allow frontend calls
public class RoomController {
    private final RoomService roomService;

    private final UserRepository userRepository;

    public RoomController(UserRepository userRepository,RoomService roomService) {
        this.roomService = roomService;
        this.userRepository=userRepository;
    }

    @GetMapping
    public List<Room> getRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping("/book")
    public Room bookRoom(@RequestBody Map<String, Long> request, Authentication authentication) throws Exception {

        Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
        //Hibernate.initialize(optionalUser);
        if (optionalUser.isEmpty()) {
           throw  new NotFoundException("User not found!");
        }        return roomService.bookRoom(request.get("roomId"), Long.valueOf(optionalUser.get().getId()));
    }

    @PostMapping("/unbook")
    public Room unbookRoom(@RequestBody Map<String, Long> request,Authentication authentication) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
        //Hibernate.initialize(optionalUser);
        if (optionalUser.isEmpty()) {
            throw  new NotFoundException("User not found!");
        }
        return roomService.unbookRoom(request.get("roomId"), Long.valueOf(optionalUser.get().getId()));
    }
}
