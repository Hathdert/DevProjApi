package com.Natixis.SkillBridge.controllers;

import com.Natixis.SkillBridge.Service.DocumentService;
import com.Natixis.SkillBridge.Repository.CandidateRepository;
import com.Natixis.SkillBridge.Repository.CompanyRepository;
import com.Natixis.SkillBridge.model.Document;
import com.Natixis.SkillBridge.model.user.Candidate;
import com.Natixis.SkillBridge.model.user.Company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
     private CompanyRepository companyRepository;

    @PostMapping("/upload/candidate")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("candidateId") Long candidateId) {
        try {
            Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
            if (!candidateOpt.isPresent()) {
                return ResponseEntity.status(404).body("Candidate not found with id: " + candidateId);
            }

            Candidate candidate = candidateOpt.get();
            Document savedDoc = documentService.saveDocumentCandidate(file, candidate);
            return ResponseEntity.ok(savedDoc);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }

    @PostMapping("/upload/company")
    public ResponseEntity<?> uploadFileForCompany(
            @RequestParam("file") MultipartFile file,
            @RequestParam("companyId") Long companyId) {
        try {
            Optional<Company> companyOpt = companyRepository.findById(companyId);
            if (!companyOpt.isPresent()) {
                return ResponseEntity.status(404).body("Company not found by id: " + companyId);
            }

            Company company = companyOpt.get();
            Document savedDoc = documentService.saveDocumentCompany(file, company);
            return ResponseEntity.ok(savedDoc);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file " + e.getMessage());
        }
    }

     @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.findAll();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        Optional<Document> document = documentService.findById(id);
        if (document.isPresent()) {
            return ResponseEntity.ok(document.get());
        } else {
            return ResponseEntity.status(404).body("Document not found by id: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("candidateId") Long candidateId) {
        try {
            Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
            if (!candidateOpt.isPresent()) {
                return ResponseEntity.status(404).body("Candidate not found by id: " + candidateId);
            }

            Candidate candidate = candidateOpt.get();
            Document updatedDoc = documentService.updateDocument(id, file, candidate);
            return ResponseEntity.ok(updatedDoc);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro updating document: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.ok("Documento deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro deleting: " + e.getMessage());
        }
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<?> getDocumentsByCandidate(@PathVariable Long candidateId) {
        Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
        if (!candidateOpt.isPresent()) {
            return ResponseEntity.status(404).body("Candidate not found by id: " + candidateId);
        }

        List<Document> documents = documentService.findByCandidateId(candidateId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getDocumentsByCompany(@PathVariable Long companyId) {
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (!companyOpt.isPresent()) {
            return ResponseEntity.status(404).body("Company not found by id: " + companyId);
        }

        List<Document> documents = documentService.findByCompanyId(companyId);
        return ResponseEntity.ok(documents);
    }
}