package com.Natixis.SkillBridge.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Natixis.SkillBridge.Repository.InternshipOfferRepository;
import com.Natixis.SkillBridge.Service.CompanyService;
import com.Natixis.SkillBridge.Service.UserService;
import com.Natixis.SkillBridge.model.InternshipOffer;
import com.Natixis.SkillBridge.model.user.Company;
import com.Natixis.SkillBridge.model.user.User;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/companies")
// @PreAuthorize("hasRole('COMPANY')")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private InternshipOfferRepository internshipOfferRepository;

    // Endpoint to get the profile of the authenticated company from the JWT token
    @GetMapping("/profile")
    public ResponseEntity<?> profileCompany(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not found");
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

    // Endpoint to update the profile of the authenticated company from the JWT
    // token
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

    // Endpoint to delete the profile of the authenticated company from the JWT
    // token
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
            return ResponseEntity.status(404).body("Company not found");
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
        return ResponseEntity.ok("Company deleted");
    }

    @GetMapping("/top6-by-applications")
    public List<Company> getTop6CompaniesByApplications() {
        return companyService.getTopCompaniesByApplications(6);
    }

    @PutMapping("/{id}/approval-status")
    public ResponseEntity<?> updateCompanyApprovalStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        Integer status = request.get("approvalStatus");

        if (status == null || status < 0 || status > 2) {
            return ResponseEntity.badRequest()
                    .body("Invalid status. Must be 0 (Pending), 1 (Approved), or 2 (Rejected)");
        }

        try {
            Company updatedCompany = companyService.updateApprovalStatus(id, status);
            return ResponseEntity.ok(updatedCompany);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/by-offer/{offerId}")
    public ResponseEntity<?> getCompanyByOfferId(@PathVariable Long offerId) {
        Optional<InternshipOffer> offerOpt = internshipOfferRepository.findById(offerId);
        if (offerOpt.isPresent()) {
            Company company = offerOpt.get().getCompany();
            if (company != null) {
                return ResponseEntity.ok(company);
            } else {
                return ResponseEntity.status(404).body("Company not found for this offer");
            }
        } else {
            return ResponseEntity.status(404).body("Offer not found");
        }
    }

    @PostMapping("/id-by-email")
    public ResponseEntity<?> getCompanyIdByEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        try {
            Long companyId = companyService.getCompanyIdByEmail(email);
            return ResponseEntity.ok(companyId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
