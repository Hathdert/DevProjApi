package com.Natixis.SkillBridge.controllers;

import com.Natixis.SkillBridge.Service.ApplicationService;
import com.Natixis.SkillBridge.Service.CandidateService;
import com.Natixis.SkillBridge.Service.CompanyService;
import com.Natixis.SkillBridge.Service.DocumentService;
import com.Natixis.SkillBridge.Repository.ApplicationRepository;
import com.Natixis.SkillBridge.Repository.CandidateRepository;
import com.Natixis.SkillBridge.Repository.CompanyRepository;
import com.Natixis.SkillBridge.model.Application;
import com.Natixis.SkillBridge.model.Document;
import com.Natixis.SkillBridge.model.user.Candidate;
import com.Natixis.SkillBridge.model.user.Company;

import io.jsonwebtoken.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    private CandidateService candidateService;

    private CompanyService companyService;

    public DocumentController(CandidateService candidateService, CompanyService companyService) {
        this.candidateService = candidateService;
        this.companyService = companyService;
    }

    @PostMapping("/upload/candidate")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("candidateEmail") String candidateEmail) {
        try {

            Long candidateId = candidateService.getCandidateIdByEmail(candidateEmail);
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
            @RequestParam("companyEmail") String companyEmail) {
        try {

            Long companyId = companyService.getCompanyIdByEmail(companyEmail);
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

    @PostMapping("/upload/application")
    public ResponseEntity<?> uploadFileForApplication(
            @RequestParam("file") MultipartFile file,
            @RequestParam("applicationId") Long applicationId) {
        try {
            Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
            if (!applicationOpt.isPresent()) {
                return ResponseEntity.status(404).body("Application not found with id: " + applicationId);
            }
            Application application = applicationOpt.get();
            Document savedDoc = documentService.saveDocumentApplication(file, application);
            return ResponseEntity.ok(savedDoc);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
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

    @GetMapping("/candidate/{candidateId}/first-image")
    public ResponseEntity<?> getFirstImageDocumentByCandidate(@PathVariable Long candidateId)
            throws java.io.IOException {
        Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
        if (!candidateOpt.isPresent()) {
            return ResponseEntity.status(404).body("Candidate not found with id: " + candidateId);
        }

        Optional<Document> imageDocumentOpt = documentService.findFirstImageByCandidate(candidateId);
        if (imageDocumentOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No image document found for candidate.");
        }

        Document imageDocument = imageDocumentOpt.get();
        Path imagePath = Paths.get(imageDocument.getFilePath());

        try {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            MediaType mediaType = getMediaTypeForFileName(imageDocument.getFileName());

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao ler a imagem: " + e.getMessage());
        }
    }

    private MediaType getMediaTypeForFileName(String fileName) {
        if (fileName.toLowerCase().endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @GetMapping("/company/{companyId}/first-image")
    public ResponseEntity<?> getFirstImageDocumentByCompany(@PathVariable Long companyId) throws java.io.IOException {
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (!companyOpt.isPresent()) {
            return ResponseEntity.status(404).body("Company not found with id: " + companyId);
        }

        Optional<Document> imageDocument = documentService.findFirstImageByCompany(companyId);
        if (imageDocument.isEmpty()) {
            return ResponseEntity.status(404).body("No image document found for company.");
        }

        Document doc = imageDocument.get();
        Path imagePath = Paths.get(doc.getFilePath());

        try {
            byte[] imageBytes = Files.readAllBytes(imagePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(doc.getFileType()));
            headers.setContentLength(imageBytes.length);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading image file: " + e.getMessage());
        }
    }

    @GetMapping("/application/{applicationId}/download")
    public ResponseEntity<?> downloadDocumentByApplication(@PathVariable Long applicationId) {
        List<Document> documents = documentService.findByApplicationId(applicationId);
        if (documents.isEmpty()) {
            return ResponseEntity.status(404).body("No document found for this application.");
        }

        Document doc = documents.get(0);
        Path filePath = Paths.get(doc.getFilePath());

        try {
            byte[] fileBytes = Files.readAllBytes(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(doc.getFileType()));
            headers.setContentLength(fileBytes.length);
            headers.setContentDispositionFormData("attachment", doc.getOriginalFileName());

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error downloading file: " + e.getMessage());
        }
    }

}