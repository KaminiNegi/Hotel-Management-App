package com.spring.security.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Integer> {

  @Query("""
    SELECT t FROM Token t 
    INNER JOIN t.user u
    WHERE u.id = :id AND (t.expired = false AND t.revoked = false)
    """)
  List<Token> findAllValidTokenByUser(@Param("id") Integer id);


  Optional<Token> findByToken(String token);
}
