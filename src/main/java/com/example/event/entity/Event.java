package com.example.event.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Getter @Setter
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private LocalDateTime dateTime;
    
    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private int maxCapacity;

    private String speaker;
    private String category;
    
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonBackReference
    private List<Registration> registrations;

}
