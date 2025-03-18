package com.spring.security.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.security.users.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, nullable = false)
  private String token;

  @Enumerated(EnumType.STRING)
  private TokenType tokenType = TokenType.BEARER;

  private boolean revoked;
  private boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)  // Lazy fetch to improve performance
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore  // Prevent circular reference
  private User user;
}
