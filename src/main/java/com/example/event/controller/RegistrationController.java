package com.example.event.controller;

import com.example.event.dto.ApiResponse;
import com.example.event.entity.User;
import com.example.event.service.EventService;
import com.example.event.service.RegistrationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
@CrossOrigin
public class RegistrationController {

    private final RegistrationService regService;
    private final EventService eventService;

    @DeleteMapping("/{eventId}")
    public ApiResponse<?> unregister(
            @PathVariable Long eventId,
            @AuthenticationPrincipal User user
    ) {
        regService.unregister(user, eventId);
        return new ApiResponse<>(true, "UnRegistered Successfull",null);
    }

    @DeleteMapping("/reg/{registrationId}")
    public ApiResponse<?> unregisterd(
            @PathVariable Long registrationId,
            @AuthenticationPrincipal User user
    ) {
        regService.unregisterByRegistrationId(user, registrationId);
        return new ApiResponse<>(true, "Unregistered successfully", null);
    }

    @GetMapping("/{registrationId}")
    public ApiResponse<?> getRegistration(@PathVariable Long registrationId, Authentication auth) {
        User user = (User) auth.getPrincipal();
        var reg = regService.findByIdAndUser(registrationId, user);
        
        return new ApiResponse<>(true, "Registration found", Map.of(
            "id", reg.getId(),
            "eventId", reg.getEvent().getId(),  // 🔥 KEY FOR FRONTEND
            "eventTitle", reg.getEvent().getTitle(),
            "venue", reg.getEvent().getVenue(),
            "registeredAt", reg.getRegisteredAt()
        ));
    }

    
    @PostMapping("/{eventId}")
    public ApiResponse<?> register(
            @PathVariable Long eventId,
            Authentication auth) {

        User user = (User) auth.getPrincipal();
        regService.register(user, eventService.get(eventId));

        return new ApiResponse<>(true, "Registered successfully", null);
    }

    @GetMapping("/my")
    public ApiResponse<?> my(Authentication auth) {
        return new ApiResponse<>(true, "My registrations",
                regService.my((User) auth.getPrincipal()));
    }
    
    @GetMapping("/event/{id}/students")
    public ApiResponse<?> students(@PathVariable Long id) {
        return new ApiResponse<>(
                true,
                "Students fetched",
                eventService.studentsByEvent(id)
        );
    }
    
    @GetMapping("/my/events")
    public ApiResponse<?> myEventIds(Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<Long> eventIds = regService.myEventIds(user);
        
        return new ApiResponse<>(true, "My registered event IDs", eventIds);
    }

}
