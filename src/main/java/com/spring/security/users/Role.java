package com.spring.security.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleType name;

 /*   @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore  // Prevent circular reference
    private Set<User> users = new HashSet<>();*/


}
