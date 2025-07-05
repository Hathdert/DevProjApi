package com.Natixis.SkillBridge.Service;
import com.Natixis.SkillBridge.Repository.DocumentRepository;
import com.Natixis.SkillBridge.model.Document;
import com.Natixis.SkillBridge.model.utilizador.Candidate;
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

    private final DocumentRepository documentRepository;

    @Value("${upload.directory}")
    private String uploadDirectory;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document saveDocument(MultipartFile file, Candidate candidate) throws IOException {
    Files.createDirectories(Paths.get(uploadDirectory));

    String originalFileName = file.getOriginalFilename();
    String fileExtension = getFileExtension(originalFileName);
    String randomFileName = UUID.randomUUID().toString() + (fileExtension.isEmpty() ? "" : "." + fileExtension);
    String filePath = uploadDirectory + File.separator + randomFileName;
    Path path = Paths.get(filePath);

    // Salva o arquivo no disco
    Files.write(path, file.getBytes(), StandardOpenOption.CREATE);

    Document doc = new Document();
    doc.setFileName(randomFileName); // Nome único salvo
    doc.setOriginalFileName(originalFileName); // Nome original para exibição
    doc.setFileType(file.getContentType());
    doc.setFilePath(filePath);
    doc.setUploadDate(LocalDate.now());
    doc.setCandidate(candidate);

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
            throw new RuntimeException("Documento não encontrado com id: " + id);
        }

        Document doc = optionalDoc.get();

        // Remove arquivo antigo, se existir
        if (doc.getFilePath() != null) {
            try {
                Files.deleteIfExists(Paths.get(doc.getFilePath()));
            } catch (IOException e) {
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

        return documentRepository.save(doc);
    }

    public void deleteDocument(Long id) throws IOException {
        Optional<Document> optionalDoc = documentRepository.findById(id);
        if (optionalDoc.isEmpty()) {
            throw new RuntimeException("Documento não encontrado com id: " + id);
        }
        Document doc = optionalDoc.get();

        // Remove arquivo do disco
        if (doc.getFilePath() != null) {
            Files.deleteIfExists(Paths.get(doc.getFilePath()));
        }

        documentRepository.deleteById(id);
    }
}