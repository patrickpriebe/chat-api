package com.project.chat.user.controller;

import com.project.chat.infrastructure.security.TokenService;
import com.project.chat.user.dto.AuthRequestDTO;
import com.project.chat.user.entity.User;
import com.project.chat.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid AuthRequestDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        var auth = authenticationManager.authenticate(usernamePassword);

        User user = userRepository.findByEmail(dto.email()).orElseThrow();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(Map.of("token", token));
    }
}