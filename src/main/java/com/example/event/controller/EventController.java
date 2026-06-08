package com.example.event.controller;

import com.example.event.dto.ApiResponse;
import com.example.event.entity.Event;
import com.example.event.service.EventService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin
public class EventController {

    private final EventService service;

    @GetMapping("/available")
    public ApiResponse<?> events() {
        return new ApiResponse<>(true, "Seats Fetched Success", service.allWithSeats());
    }
    
    @GetMapping("/{id}")
    public ApiResponse<?> getEvent(@PathVariable Long id) {
        return new ApiResponse<>(true, "Event fetched", service.getEvent(id));
    }
    
    @PostMapping
    public ApiResponse<?> create(@RequestBody Event e) {
        return new ApiResponse<>(true, "Event created", service.create(e));
    }

    @GetMapping
    public ApiResponse<?> all() {
        return new ApiResponse<>(true, "Events fetched", service.all());
    }
    
    // 🔹 Get students for specific event
    @GetMapping("/{id}/students")
    public ApiResponse<?> students(@PathVariable Long id) {
        return new ApiResponse<>(true, "Registered students", service.getRegisteredStudents(id));
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody Event e) {
        return new ApiResponse<>(true, "Event updated", service.update(id, e));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        service.delete(id);
        return new ApiResponse<>(true, "Event deleted", null);
    }
}
