package com.spring.security.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.roles 
        WHERE u.email = :email
    """)
  Optional<User> findByEmail(@Param("email") String email);

  @Query("""
          SELECT u FROM User u LEFT JOIN FETCH u.roles
          """)
  List<User> findAllWithRoles();

    boolean existsByEmail(String s);
}
