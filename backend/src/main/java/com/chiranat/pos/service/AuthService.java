package com.chiranat.pos.service;

import com.chiranat.pos.dto.AuthResponse;
import com.chiranat.pos.dto.LoginRequest;
import com.chiranat.pos.dto.RegisterRequest;
import com.chiranat.pos.model.Role;
import com.chiranat.pos.model.User;
import com.chiranat.pos.repository.RoleRepository;
import com.chiranat.pos.repository.UserRepository;
import com.chiranat.pos.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        
        // Remove ROLE_ prefix if present
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        return new AuthResponse(jwt, userDetails.getUsername(), role);
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        // Create new user's account
        User user = User.builder()
                .username(registerRequest.getUsername())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .build();

        String strRole = registerRequest.getRole();
        Role role;

        if (strRole == null) {
            role = roleRepository.findByName("WAITER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            role = roleRepository.findByName(strRole)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        }

        user.setRole(role);
        userRepository.save(user);
    }
}
