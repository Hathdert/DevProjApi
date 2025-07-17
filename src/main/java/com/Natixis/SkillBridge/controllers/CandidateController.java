package com.Natixis.SkillBridge.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Natixis.SkillBridge.Service.CandidateService;
import com.Natixis.SkillBridge.Service.UserService;
import com.Natixis.SkillBridge.model.user.Candidate;
import com.Natixis.SkillBridge.model.user.Company;
import com.Natixis.SkillBridge.model.user.User;

@RestController

@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateService candidateService;
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    // Endpoints for CRUD operations
    // Get all candidates
    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profileCandidate(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        String email = authentication.getName();
        System.out.println("-------Email: " + email);
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
        System.out.println("-------User: " + user);
        Candidate company = candidateService.getCandidateById(user.getId());
        if (company == null) {
            return ResponseEntity.status(404).body("Empresa não encontrada");
        }
        System.out.println("-------Company: " + company);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfileCandidate(Authentication authentication, @RequestBody String password) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(403).body("Incorrect password");
        }
        candidateService.deleteCandidate(user.getId());
        return ResponseEntity.ok("Company profile deleted successfully");
    }

    // Get candidate by ID
    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }

    // Update candidate
    @PutMapping("/profile")
    public ResponseEntity<?> updateCandidateProfile(
            Authentication authentication,
            @RequestBody Candidate updatedCandidate) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        Candidate updated = candidateService.updateCandidate(user.getId(), updatedCandidate);
        return ResponseEntity.ok(updated);
    }

    // Delete candidate
    @DeleteMapping("/{id}")
    public void deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
    }
}
