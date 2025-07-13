package com.Natixis.SkillBridge.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Natixis.SkillBridge.Repository.DocumentRepository;
import com.Natixis.SkillBridge.model.Document;
import com.Natixis.SkillBridge.model.user.Candidate;
import com.Natixis.SkillBridge.model.user.Company;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.util.UUID;

@Service
public class DocumentService {
    Logger logger = LoggerFactory.getLogger(InternshipOfferService.class);
    private final DocumentRepository documentRepository;

    @Value("${upload.directory}")
    private String uploadDirectory;

    public DocumentService(DocumentRepository documentRepository) {
        logger.info("DocumentService initialized with upload directory: {}", uploadDirectory);
        this.documentRepository = documentRepository;
    }

    public Document saveDocumentCandidate(MultipartFile file, Candidate candidate) throws IOException {
        Files.createDirectories(Paths.get(uploadDirectory));

        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String randomFileName = UUID.randomUUID().toString() + (fileExtension.isEmpty() ? "" : "." + fileExtension);
        String filePath = uploadDirectory + File.separator + randomFileName;
        Path path = Paths.get(filePath);

        Files.write(path, file.getBytes(), StandardOpenOption.CREATE);

        Document doc = new Document();
        doc.setFileName(randomFileName);
        doc.setOriginalFileName(originalFileName);
        doc.setFileType(file.getContentType());
        doc.setFilePath(filePath);
        doc.setUploadDate(LocalDate.now());
        doc.setCandidate(candidate);
        candidate.getDocuments().add(doc);

        logger.info("Saving document for candidate: {}", candidate.getEmail()); 
        return documentRepository.save(doc);
    }

    public Document saveDocumentCompany(MultipartFile file, Company company) throws IOException {

        Files.createDirectories(Paths.get(uploadDirectory));

        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String randomFileName = UUID.randomUUID().toString() + (fileExtension.isEmpty() ? "" : "." + fileExtension);
        String filePath = uploadDirectory + File.separator + randomFileName;
        Path path = Paths.get(filePath);

        Files.write(path, file.getBytes(), StandardOpenOption.CREATE);

        Document doc = new Document();
        doc.setFileName(randomFileName);
        doc.setOriginalFileName(originalFileName);
        doc.setFileType(file.getContentType());
        doc.setFilePath(filePath);
        doc.setUploadDate(LocalDate.now());
        doc.setCompany(company);
        company.getDocuments().add(doc);

        return documentRepository.save(doc);
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Document updateDocument(Long id, MultipartFile file, Candidate candidate) throws IOException {
        Optional<Document> optionalDoc = documentRepository.findById(id);

        if (optionalDoc.isEmpty()) {
            logger.error("Document not found with id: {}", id);
            throw new RuntimeException("Document not found with id: " + id);
        }

        Document doc = optionalDoc.get();

        if (doc.getFilePath() != null) {
            try {
                Files.deleteIfExists(Paths.get(doc.getFilePath()));
            } catch (IOException e) {
                logger.error("Error deleting existing file: {}", doc.getFilePath(), e);
                throw new RuntimeException("Could not delete existing file: " + doc.getFilePath(), e);
            }
        }

        Files.createDirectories(Paths.get(uploadDirectory));

        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName;
        String filePath = uploadDirectory + File.separator + fileName;
        Path path = Paths.get(filePath);

        Files.write(path, file.getBytes(), StandardOpenOption.CREATE);

        doc.setFileName(fileName);
        doc.setFileType(file.getContentType());
        doc.setFilePath(filePath);
        doc.setUploadDate(LocalDate.now());
        doc.setCandidate(candidate);
        candidate.getDocuments().add(doc);

        logger.info("Updating document with ID: {}", id);
        return documentRepository.save(doc);
    }

    public void deleteDocument(Long id) throws IOException {
        Optional<Document> optionalDoc = documentRepository.findById(id);
        if (optionalDoc.isEmpty()) {
            logger.error("Document not found with id: {}", id);
            throw new RuntimeException("Document not found with id: " + id);
        }
        Document doc = optionalDoc.get();

        if (doc.getFilePath() != null) {
            logger.info("Deleting file: {}", doc.getFilePath());
            Files.deleteIfExists(Paths.get(doc.getFilePath()));
        }

        documentRepository.deleteById(id);
    }

    // Return all documents from candidate ID
    public List<Document> findByCandidateId(Long candidateId) {
        return documentRepository.findByCandidateId(candidateId);
    }

    // Return all documents from company ID
    public List<Document> findByCompanyId(Long companyId) {
        return documentRepository.findByCompanyId(companyId);
    }

    public Optional<Document> findFirstImageByCandidate(Long candidateId) {
        List<Document> documents = documentRepository.findByCandidateId(candidateId);

        return documents.stream()
                .filter(doc -> doc.getFileType() != null && doc.getFileType().startsWith("image/"))
                .findFirst();
    }

    public Optional<Document> findFirstImageByCompany(Long companyId) {
        List<Document> documents = documentRepository.findByCompanyId(companyId);
        return documents.stream()
                .filter(doc -> doc.getFileType() != null && doc.getFileType().startsWith("image"))
                .findFirst();
    }
}