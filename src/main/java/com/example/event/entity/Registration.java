package com.example.event.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter @Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","event_id"}))
public class Registration {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Event event;

    private LocalDateTime registeredAt;
}
