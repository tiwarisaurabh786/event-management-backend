package com.example.event.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.event.entity.Event;
import com.example.event.entity.Registration;
import com.example.event.entity.User;
import com.example.event.exception.BadRequestException;
import com.example.event.exception.ResourceNotFoundException;
import com.example.event.repository.EventRepository;
import com.example.event.repository.RegistrationRepository;

@Service
public class RegistrationService {

    private final RegistrationRepository regRepo;
    private final EventRepository eventRepo;

    public RegistrationService(EventRepository eventRepo,RegistrationRepository regRepo) {
        this.regRepo = regRepo;
        this.eventRepo = eventRepo;
    }

      
    public void unregister(User user, Long eventId) {

        Registration r = regRepo.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));

        regRepo.delete(r);
    }
    public List<Long> myEventIds(User user) {
        return regRepo.findByUser(user).stream()
            .map(reg -> reg.getEvent().getId())
            .distinct()
            .collect(Collectors.toList());
    }

    
    public void unregisterByRegistrationId(User user, Long registrationId) {
        Registration r = findByIdAndUser(registrationId, user);
        regRepo.delete(r);
    }
    
    public Registration findByIdAndUser(Long registrationId, User user) {
        return regRepo.findByIdAndUser(registrationId, user)
            .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
    }


    public void register(User user, Event event) {

    	
    	if (user == null || event == null)
            throw new BadRequestException("Invalid registration");
    	
        if (regRepo.existsByUserAndEvent(user, event))
            throw new BadRequestException("Already registered");

        if (regRepo.countByEvent(event) >= event.getMaxCapacity())
            throw new BadRequestException("Event full");

        Registration r = new Registration();
        r.setUser(user);
        r.setEvent(event);
        r.setRegisteredAt(LocalDateTime.now());

        regRepo.save(r);
    }
    public List<Registration> my(User user) {
        return regRepo.findByUser(user);
    }
}
