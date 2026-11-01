package com.notMilanGurung.banana.backend.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
  @Id 
  @GeneratedValue(strategy = GenerationType.UUID)
  @Getter private UUID id; 

  @NotBlank
  @Column(nullable = false, unique = true)
  @Getter @Setter private String username;

  @Email 
  @Column(unique = true)
  @Getter @Setter private String email; // Optional

  @NotBlank 
  @Column(nullable = false)
  @Getter @Setter private String passwordHash;

  @Column(nullable = false, columnDefinition = "integer default 0")
  @Getter @Setter private int highScore = 0;

  @CreatedDate 
  @Column(nullable = false, updatable = false)
  @Getter private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  @Getter private LocalDateTime updatedAt;

}