package com.Natixis.SkillBridge.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Natixis.SkillBridge.Service.CompanyService;
import com.Natixis.SkillBridge.Service.UserService;
import com.Natixis.SkillBridge.model.utilizador.Company;
import com.Natixis.SkillBridge.model.utilizador.User;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/companies")
// @PreAuthorize("hasRole('COMPANY')")
public class CompanyController {
    
    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    // Endpoint to get the profile of the authenticated company from the JWT token
    @GetMapping("/profile")
    public ResponseEntity<?> profileCompany(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        Company company = companyService.getCompanyById(user.getId());
        if (company == null) {
            return ResponseEntity.status(404).body("Company not found");
        }
        return ResponseEntity.ok(company);
    }

    // Endpoint to update the profile of the authenticated company from the JWT token
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfileCompany(@RequestBody Company updatedCompany, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        Company existingCompany = companyService.getCompanyById(user.getId());
        if (existingCompany == null) {
            return ResponseEntity.status(404).body("Company not found");
        }

        Company updated = companyService.updateCompany(user.getId(), updatedCompany);
        return ResponseEntity.ok(updated);
    }

    // Endpoint to delete the profile of the authenticated company from the JWT token
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfileCompany(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        
        companyService.deleteCompany(user.getId());
        return ResponseEntity.ok("Company profile deleted successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> profileCompany(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        if (company == null) {
            return ResponseEntity.status(404).body("Empresa não encontrada");
        }

        return ResponseEntity.ok(company);
    }

    @PutMapping("/{id}")
    public Company updateCompnay(@PathVariable Long id, @RequestBody Company updatedCompany) {
        System.out.println("idcandidate " + profileCompany(id));
        return companyService.updateCompany(id, updatedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok("Empresa deletada com sucesso");
    }
}
