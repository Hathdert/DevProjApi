package com.Natixis.SkillBridge.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Natixis.SkillBridge.Service.UserService;

import jakarta.validation.Valid;

import com.Natixis.SkillBridge.controllers.AuthRequest;
import com.Natixis.SkillBridge.model.user.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

//   @PostMapping("/register")
// public ResponseEntity<?> register(@RequestBody AuthRequest request) {
//     User user = new User();
//     user.setName(request.getUsername());
//     user.setEmail(request.getEmail());
//     user.setPassword(passwordEncoder.encode(request.getPassword()));
//     user.setRole("ROLE_CADIDATE"); // default value


//      try {
//         User savedUser = userService.createUser(user); // Assuming createUser returns the saved User object
//         return ResponseEntity.status(HttpStatus.CREATED).body(savedUser); // e pegue o ID daqui
//     } catch (DataIntegrityViolationException e) {
//         return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado");
//     }
// }

@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody UserRequest request) {
    System.out.println("JSON recebido (convertido para UserRequest): " + request);

    try {
        userService.registerUser(request);
        return ResponseEntity.ok("Usuário registrado com sucesso");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

}
