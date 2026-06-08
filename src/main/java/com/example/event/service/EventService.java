package com.example.event.service;

import com.example.event.entity.Event;
import com.example.event.entity.Registration;
import com.example.event.entity.User;
import com.example.event.exception.*;
import com.example.event.repository.EventRepository;
import com.example.event.repository.RegistrationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repo;
    private final RegistrationRepository regRepo;

    
    public Event create(Event e) {
        if (e == null || e.getTitle() == null || e.getVenue() == null || e.getMaxCapacity() <= 0)
            throw new BadRequestException("Invalid event data");

        return repo.save(e);
    }

    public List<Event> all() {
        return repo.findAll();
    }

    public List<Map<String, Object>> allWithSeats() {

        return repo.findAll().stream().map(e -> {

            long used = regRepo.countByEvent(e);
            long left = Math.max(0, e.getMaxCapacity() - used);

            Map<String, Object> map = new HashMap<>();
            map.put("id", e.getId());
            map.put("title", e.getTitle());
            map.put("venue", e.getVenue());
            map.put("maxCapacity", e.getMaxCapacity());
            map.put("remainingSeats", left);
            map.put("category", e.getCategory());
            map.put("speaker", e.getSpeaker());
            map.put("date_time", e.getDateTime());
            map.put("description", e.getDescription());
            return map;
        }).toList();
    }

    public Event update(Long id, Event e) {
        Event db = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (e.getTitle() != null) db.setTitle(e.getTitle());
        if (e.getDescription() != null) db.setDescription(e.getDescription());
        if (e.getDateTime() != null) db.setDateTime(e.getDateTime());
        if (e.getVenue() != null) db.setVenue(e.getVenue());
        if (e.getSpeaker() != null) db.setSpeaker(e.getSpeaker());
        if (e.getCategory() != null) db.setCategory(e.getCategory());
        if (e.getMaxCapacity() > 0) db.setMaxCapacity(e.getMaxCapacity());

        return repo.save(db);
    }
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("Event not found");
        repo.deleteById(id);
    }
    
    public Event getEvent(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }
    // ADMIN: get students for event
    public List<User> getRegisteredStudents(Long eventId) {

        Event event = getEvent(eventId);

        return regRepo.findByEvent(event)
                .stream()
                .map(Registration::getUser)
                .toList();
    }
    
    public Event get(Long id) {
        if (id == null)
            throw new BadRequestException("Invalid event id");

        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }
    
    public List<User> studentsByEvent(Long eventId) {
        Event event = repo.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        return regRepo.findByEvent(event)
                .stream()
                .map(Registration::getUser)
                .toList();
    }
}
