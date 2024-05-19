package com.subsaw.ilearn.security.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @SequenceGenerator(allocationSize = 1, sequenceName = "seq_user_id", name = "seq_user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_id")
    private long UserId;

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user")
    private List<UserRole> roles;
}
