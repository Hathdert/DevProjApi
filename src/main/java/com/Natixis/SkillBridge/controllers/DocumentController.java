package com.Natixis.SkillBridge.controllers;

import com.Natixis.SkillBridge.Service.DocumentService;
import com.Natixis.SkillBridge.Repository.CandidateRepository;
import com.Natixis.SkillBridge.model.Document;
import com.Natixis.SkillBridge.model.utilizador.Candidate;
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

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("candidateId") Long candidateId) {
        try {
            Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
            if (!candidateOpt.isPresent()) {
                return ResponseEntity.status(404).body("Candidate não encontrado com id: " + candidateId);
            }

            Candidate candidate = candidateOpt.get();
            Document savedDoc = documentService.saveDocument(file, candidate);
            return ResponseEntity.ok(savedDoc);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao fazer upload do arquivo: " + e.getMessage());
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
            return ResponseEntity.status(404).body("Documento não encontrado com id: " + id);
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
                return ResponseEntity.status(404).body("Candidate não encontrado com id: " + candidateId);
            }

            Candidate candidate = candidateOpt.get();
            Document updatedDoc = documentService.updateDocument(id, file, candidate);
            return ResponseEntity.ok(updatedDoc);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao atualizar documento: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.ok("Documento deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao deletar documento: " + e.getMessage());
        }
    }
}