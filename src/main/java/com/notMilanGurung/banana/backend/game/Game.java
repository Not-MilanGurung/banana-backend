package com.notMilanGurung.banana.backend.game;

import com.notMilanGurung.banana.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "games")
@EntityListeners(AuditingEntityListener.class)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter private UUID id;

    @Column(nullable = false)
    @Getter  @Setter private int streak = 0;

    @Column(nullable = false)
    @Getter @Setter private boolean active = true;

    @Column(nullable = false)
    @Getter @Setter private String currentQuestion;

    @Column(nullable = false)
    @Getter @Setter private int currentSolution;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Getter private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter @Setter private User user; // Links game record to a specific user

}