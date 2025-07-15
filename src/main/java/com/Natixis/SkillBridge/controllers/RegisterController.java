package com.Natixis.SkillBridge.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.Natixis.SkillBridge.Service.UserService;

import jakarta.validation.Valid;


import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    private UserService userService;


    @GetMapping("/check-email")
        public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
            boolean taken = userService.findByEmail(email) != null;
            Map<String, Boolean> response = Collections.singletonMap("taken", taken);
            return ResponseEntity.ok(response);
        }

@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody UserRequest request) {
   if (userService.existsByEmail(request.getEmail())) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", "E-mail already in use"));
    }
    System.out.println("JSON recebido (convertido para UserRequest): " + request);

    try {
        userService.registerUser(request);
        return ResponseEntity.ok("Usuário registrado com sucesso");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

}
