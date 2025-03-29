package com.spring.security.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;


    @GetMapping
    @Transactional
    public ResponseEntity<List<EntityModel<User>>> getAllUsers(Authentication authentication) {
        Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
        Hibernate.initialize(optionalUser);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User currentUser = optionalUser.get();
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName().name()));

        List<User> users= new ArrayList<>() ;
        if(isAdmin) {
            users = userRepository.findAllWithRoles();
        }
        else {
            users = List.of(currentUser);
        }

        users.forEach(user -> Hibernate.initialize(user.getRoles()));

        List<EntityModel<User>> userModels = users.stream()
                .peek(user -> {
       
                    String rolesString = user.getRoles().stream()
                            .map(role -> role.getName().name()) 
                            .collect(Collectors.joining(", "));

                    user.setRolesString(rolesString);

                    System.out.println("User: " + user.getEmail() + " -> Roles: " + user.getRoles()); // Debug print
                })
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAllUsers(authentication)).withRel("all-users")
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userModels);
    }




    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUserById(@PathVariable Integer id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        EntityModel<User> userModel = EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers(null)).withRel("all-users")
        );

        return ResponseEntity.ok(userModel);
    }
}
