package com.waa.project.service.impl;

import com.waa.project.dto.requestDto.LoginRequestDto;
import com.waa.project.dto.responseDto.LoginResponseDto;
import com.waa.project.dto.responseDto.RegistrationDto;
import com.waa.project.dto.responseDto.UsersDto;
import com.waa.project.entity.User;
import com.waa.project.entity.UserRole;
import com.waa.project.repository.UserRepository;
import com.waa.project.service.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto){
        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());
        if(!optionalUser.isPresent()){
            return ResponseEntity.badRequest().body("Invalid email");
        }
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), optionalUser.get().getPassword())){
            return ResponseEntity.badRequest().body("Invalid password");
        }
        String token = jwtTokenService.generateToken(optionalUser.get());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    public ResponseEntity<?> registerStudent(RegistrationDto registrationDto) {
        if(userRepository.findByEmail(registrationDto.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("Email already exist");
        }
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setUserRole(UserRole.ALUMNI);
        user.setProfile(registrationDto.getProfile());
        user = userRepository.save(user);
        return ResponseEntity.ok(UsersDto.fromUser(user));
    }
}