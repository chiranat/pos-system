package com.chiranat.pos.controller;

import com.chiranat.pos.model.DiningSession;
import com.chiranat.pos.repository.DiningSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/public/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final DiningSessionRepository diningSessionRepository;

    @GetMapping("/session/{token}")
    public ResponseEntity<?> getSessionByToken(@PathVariable String token) {
        return diningSessionRepository.findByAccessToken(token)
                .map(session -> {
                    if (session.getStatus() != DiningSession.SessionStatus.ACTIVE) {
                        return ResponseEntity.badRequest().body(Map.of("message", "Session is not active"));
                    }
                    return ResponseEntity.ok(session);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
