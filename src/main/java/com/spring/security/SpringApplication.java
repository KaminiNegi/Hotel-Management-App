package com.spring.security;

import com.spring.security.booking.Room;
import com.spring.security.booking.RoomRepository;
import com.spring.security.users.Role;
import com.spring.security.users.RoleRepository;
import com.spring.security.users.RoleType;
import com.spring.security.users.User;
import com.spring.security.users.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
	}
	private final RoleRepository roleRepository;

	private  final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final RoomRepository roomRepository;

	@Override
	public void run(String... args) throws Exception {
		if (roleRepository.count() == 0) {
			List<Role> roles = List.of(
					new Role(null, RoleType.USER),
					new Role(null, RoleType.ADMIN),
					new Role(null, RoleType.MANAGER)
			);
			roleRepository.saveAll(roles);
		}
		if (!userRepository.existsByEmail("admin@gmail.com")) {
			Role adminRole = roleRepository.findByName(RoleType.ADMIN)
					.orElseThrow(() -> new RuntimeException("Default ADMIN role not found"));

			var user = User.builder()
					.firstname("admin")
					.lastname("admin")
					.email("admin@gmail.com")
					.password(passwordEncoder.encode("admin"))
					.roles(Set.of(adminRole))
					.createdAt(new Date())
					.build();
			userRepository.save(user);
		}

		if (roomRepository.count() == 0) {
			for (int i = 1; i <= 12; i++) {
				roomRepository.save(new Room(null, i, false, null));
			}
		}
	}

	
}
