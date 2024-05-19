package com.subsaw.ilearn.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserRole {

    @Id
    @SequenceGenerator(allocationSize = 1, sequenceName = "seq_userrole_id", name="seq_userrole_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_userrole_id")
    private long userRoleId;

    private String role;
    private String desciption;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
}
