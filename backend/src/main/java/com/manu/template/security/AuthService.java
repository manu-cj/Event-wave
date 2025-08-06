package com.manu.template.security;

import com.manu.template.dto.JwtResponseDTO;
import com.manu.template.dto.UserLoginDTO;
import com.manu.template.model.User;
import com.manu.template.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public JwtResponseDTO authenticate(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        String token = jwtUtil.generateToken(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(),user.getRoles());
        return new JwtResponseDTO(token);
    }

    public ResponseEntity<JwtResponseDTO> authenticateAndGetToken(UserLoginDTO userLoginDTO, HttpServletResponse response) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        // Authentifie l'utilisateur et génère le JWT
        String jwt = jwtUtil.generateToken(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(),user.getRoles());/* logique pour générer le JWT */;
        JwtResponseDTO jwtResponse = new JwtResponseDTO(jwt);

        // Crée le cookie JWT
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        // cookie.setSecure(true); // Active en prod
        response.addCookie(cookie);

        return ResponseEntity.ok(jwtResponse);
    }
}