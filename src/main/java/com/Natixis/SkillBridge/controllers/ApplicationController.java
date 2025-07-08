package com.Natixis.SkillBridge.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Natixis.SkillBridge.Service.ApplicationService;
import com.Natixis.SkillBridge.model.Application;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {
    
    @Autowired
    private ApplicationService applicationService;

    // List all applications
    @GetMapping
    public List<Application> getAllApplications() {
        return applicationService.listAll();
    }

    // Search application by ID
    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Application application = applicationService.findById(id);
        if (application != null) {
            return ResponseEntity.ok(application);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // List all applications by candidate ID
    @GetMapping("/candidate/{candidateId}")
    public List<Application> getApplicationsByCandidateId(@PathVariable Long candidateId) {
        return applicationService.listByCandidateId(candidateId);
    }

    // List all applications inside an internship offer
    @GetMapping("/internshipoffer/{internshipOfferId}")
    public List<Application> getApplicationsByInternshipOfferId(Long internshipOfferId) {
        return applicationService.listByInternshipOfferId(internshipOfferId);
    }

    // Create new application
    @PostMapping("/new")
    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
        Application createdApplication = applicationService.makeNew(application);
        return ResponseEntity.ok(createdApplication);
    }

    // Update application
    @PutMapping("/update/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application application) {
        Application updatedApplication = applicationService.update(id, application);
        return ResponseEntity.ok(updatedApplication);
    }

    // Delete application
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
