package com.Natixis.SkillBridge.controllers;

import com.Natixis.SkillBridge.Service.InternshipOfferService;
import com.Natixis.SkillBridge.model.InternshipOffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/internshipoffers")
@CrossOrigin(origins = "http://localhost:4200")
public class InternshipOfferController {

    @Autowired
    private InternshipOfferService service;

    // List all InternshipOffer
    @GetMapping
    public List<InternshipOffer> getAllOffers() {
        return service.findAll();
    }

    // Search InternshipOffer by ID
    @GetMapping("/{id}")
    public ResponseEntity<InternshipOffer> getOfferById(@PathVariable Long id) {
        InternshipOffer offer = service.findById(id);
        if (offer != null) {
            return ResponseEntity.ok(offer);
        } else {
            return ResponseEntity.notFound().build();
        }
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
        InternshipOffer existingOffer = service.findById(id);

        if (existingOffer == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        if (!service.checkPendentApplications(existingOffer)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 - can't update due to pending apps
        }

        InternshipOffer updatedOffer = service.update(id, offer);
        return ResponseEntity.ok(updatedOffer); // 200
    }


    // Delete InternshipOffer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        InternshipOffer existingOffer = service.findById(id);

        if (existingOffer == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        if (!service.checkPendentApplications(existingOffer)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 - can't delete due to pending apps
        }

        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/top6-by-applications")
public List<InternshipOffer> getTop6OffersByApplications() {
    return service.getTopOffersByApplications(6);
}
}
