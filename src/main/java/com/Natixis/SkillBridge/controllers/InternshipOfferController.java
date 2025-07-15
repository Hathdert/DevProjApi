package com.Natixis.SkillBridge.controllers;

import com.Natixis.SkillBridge.Service.CompanyService;
import com.Natixis.SkillBridge.Service.InternshipOfferService;
import com.Natixis.SkillBridge.Service.UserService;
import com.Natixis.SkillBridge.model.Application;
import com.Natixis.SkillBridge.model.InternshipOffer;
import com.Natixis.SkillBridge.model.user.Candidate;
import com.Natixis.SkillBridge.model.user.Company;
import com.Natixis.SkillBridge.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/internshipoffers")
@CrossOrigin(origins = "http://localhost:4200")
public class InternshipOfferController {

    @Autowired
    private InternshipOfferService service;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    // List all InternshipOffer
    @GetMapping
    public List<InternshipOffer> getAllOffers() {
        return service.findAll();
    }

    // Search InternshipOffer by ID
    @GetMapping("/{id}")
    public ResponseEntity<InternshipOffer> getOfferById(@PathVariable Long id) {
        Optional<InternshipOffer> offerOpt = service.findById(id);
        return offerOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new InternshipOffer
    @PostMapping
    public ResponseEntity<InternshipOffer> createOffer(@RequestBody InternshipOffer offer) {
        InternshipOffer savedOffer = service.save(offer);
        return ResponseEntity.ok(savedOffer);
    }

    // Update InternshipOffer
    @PutMapping("/{id}")
    public ResponseEntity<InternshipOffer> updateOffer(@PathVariable Long id, @RequestBody InternshipOffer offer) {
        InternshipOffer updatedOffer = service.update(id, offer);
        if (updatedOffer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOffer);
    }

    // Delete InternshipOffer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/top6-by-applications")
    public List<InternshipOffer> getTop6OffersByApplications() {
        return service.getTopOffersByApplications(6);
    }

    @GetMapping("companies/{companyId}")
    public List<InternshipOffer> getOffersByCompanyId(@PathVariable Long companyId) {
        return service.findByCompanyId(companyId);
    }

    @PatchMapping("/offer/{offerId}/change-status")
    public ResponseEntity<InternshipOffer> changeOfferStatus( @PathVariable Long offerId, @RequestBody boolean status) {
        
        InternshipOffer updatedOffer = service.changeStatus(offerId, status);
        if (updatedOffer != null) {
            return ResponseEntity.ok(updatedOffer);
        }
        return ResponseEntity.notFound().build();
    }

@GetMapping("token/{id}")
public ResponseEntity<?> getOfferByIdToken(@PathVariable Long id, Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(401).body("User not found");
    }

    String email = authentication.getName();
    User user = userService.findByEmail(email);

    if (user == null) {
        return ResponseEntity.status(404).body("User not found");
    }

    Optional<InternshipOffer> offerOpt = service.findById(id);

    if (offerOpt.isEmpty()) {
        return ResponseEntity.status(404).body("Offer not found");
    }

    InternshipOffer offer = offerOpt.get();

    Map<String, Object> offerMap = new HashMap<>();
    offerMap.put("id", offer.getId());
    offerMap.put("title", offer.getTitle());
    offerMap.put("description", offer.getDescription());
    offerMap.put("requirements", offer.getRequirements());
    offerMap.put("area", offer.getArea());
    offerMap.put("startDate", offer.getStartDate());
    offerMap.put("endDate", offer.getEndDate());
    offerMap.put("vacancies", offer.getVacancies());
    offerMap.put("offer", offer.isOffer());

    List<Map<String, Object>> applicationsList = offer.getApplications().stream().map(app -> {
        Map<String, Object> appMap = new HashMap<>();
        appMap.put("id", app.getId());
        appMap.put("pitch", app.getPitch());
        appMap.put("state", app.getState());
        appMap.put("candidateId", app.getCandidate() != null ? app.getCandidate().getId() : null);
        appMap.put("document", app.getDocument());
        return appMap;
    }).toList();

    offerMap.put("applications", applicationsList);

    return ResponseEntity.ok(Map.of("offer", offerMap));
}


}
