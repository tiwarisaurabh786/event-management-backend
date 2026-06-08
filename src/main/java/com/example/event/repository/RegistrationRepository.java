package com.example.event.repository;

import com.example.event.entity.Event;
import com.example.event.entity.Registration;
import com.example.event.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserAndEvent(User user, Event event);

    List<Registration> findByUser(User user);


    Optional<Registration> findByIdAndUser(Long id, User user);
    
    long countByEvent(Event event);

    List<Registration> findByEvent(Event event);
    
    Optional<Registration> findByUserAndEvent(User user, Event event);
}
