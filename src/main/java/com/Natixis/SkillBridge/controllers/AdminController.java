package com.Natixis.SkillBridge.controllers;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Natixis.SkillBridge.Service.ApplicationService;
import com.Natixis.SkillBridge.Service.CandidateService;
import com.Natixis.SkillBridge.Service.CompanyService;
import com.Natixis.SkillBridge.Service.DocumentService;
import com.Natixis.SkillBridge.Service.InternshipOfferService;
import com.Natixis.SkillBridge.model.Application;
import com.Natixis.SkillBridge.model.Document;
import com.Natixis.SkillBridge.model.InternshipOffer;
import com.Natixis.SkillBridge.model.user.Candidate;
import com.Natixis.SkillBridge.model.user.Company;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private InternshipOfferService internshipOfferService;

    // DTO para aprovação
    public static class ApprovalStatusDTO {
        public int newStatus;
    }

    // Endpoint para alterar approvalStatus
    @PutMapping("/companies/{id}/approval-status")
    public ResponseEntity<?> updateCompanyApprovalStatus(@PathVariable Long id, @RequestBody ApprovalStatusDTO dto) {
        try {
            Company updatedCompany = companyService.updateApprovalStatus(id, dto.newStatus);
            return ResponseEntity.ok(updatedCompany);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Endpoint para retornar todos os candidatos
    @GetMapping("/candidates")
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    // Endpoint para retornar todas as empresas
    @GetMapping("/companies")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    // CSVS

    @GetMapping("/candidates/csv")
    public void exportCandidatesToCSV(HttpServletResponse response) throws IOException, java.io.IOException {
        setCSVHeaders(response, "candidates.csv");

        List<Candidate> candidates = candidateService.getAllCandidates();
        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,Name,Email,Role,RegistrationDate,RegistrationTime,Address,Phone,BirthDate");
            for (Candidate c : candidates) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        c.getId(),
                        escapeCsv(c.getName()),
                        c.getEmail(),
                        c.getRole(),
                        c.getRegistrationDate(),
                        c.getRegistrationTime(),
                        escapeCsv(c.getAddress()),
                        c.getPhone(),
                        c.getBirthDate());
            }
        }
    }

    @GetMapping("/companies/csv")
    public void exportCompaniesToCSV(HttpServletResponse response) throws IOException, java.io.IOException {
        setCSVHeaders(response, "companies.csv");

        List<Company> companies = companyService.getAllCompanies();
        try (PrintWriter writer = response.getWriter()) {
            writer.println(
                    "ID,Name,Email,Role,RegistrationDate,RegistrationTime,Address,Phone,Description,Area,ApprovalStatus");
            for (Company c : companies) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d%n",
                        c.getId(),
                        escapeCsv(c.getName()),
                        c.getEmail(),
                        c.getRole(),
                        c.getRegistrationDate(),
                        c.getRegistrationTime(),
                        escapeCsv(c.getAddress()),
                        c.getPhone(),
                        escapeCsv(c.getDescription()),
                        escapeCsv(c.getArea()),
                        c.getApprovalStatus());
            }
        }
    }

    @GetMapping("/applications/csv")
    public void exportApplicationsToCSV(HttpServletResponse response) throws IOException, java.io.IOException {
        setCSVHeaders(response, "applications.csv");

        List<Application> applications = applicationService.listAll();
        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,CandidateID,InternshipOfferID,Pitch,State,DocumentID");
            for (Application a : applications) {
                writer.printf("%d,%s,%s,%s,%s,%s%n",
                        a.getId(),
                        a.getCandidate() != null ? a.getCandidate().getId() : "",
                        a.getInternshipOffer() != null ? a.getInternshipOffer().getId() : "",
                        escapeCsv(a.getPitch()),
                        a.getState(),
                        a.getDocument() != null && !a.getDocument().isEmpty() ? a.getDocument().get(0).getId() : "");}
        }
    }

    @GetMapping("/documents/csv")
    public void exportDocumentsToCSV(HttpServletResponse response) throws IOException, java.io.IOException {
        setCSVHeaders(response, "documents.csv");

        List<Document> documents = documentService.findAll();
        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,OriginalFileName,FileName,FileType,UploadDate,FilePath,CandidateID,CompanyID");
            for (Document d : documents) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s%n",
                        d.getId(),
                        escapeCsv(d.getOriginalFileName()),
                        d.getFileName(),
                        d.getFileType(),
                        d.getUploadDate(),
                        escapeCsv(d.getFilePath()),
                        d.getCandidate() != null ? d.getCandidate().getId() : "",
                        d.getCompany() != null ? d.getCompany().getId() : "");
            }
        }
    }

    @GetMapping("/internship-offers/csv")
    public void exportInternshipOffersToCSV(HttpServletResponse response) throws IOException, java.io.IOException {
        setCSVHeaders(response, "internship_offers.csv");

        List<InternshipOffer> offers = internshipOfferService.findAll();
        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,Title,Description,Requirements,Area,StartDate,EndDate,Vacancies,Offer,CompanyID");
            for (InternshipOffer o : offers) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%d,%b,%s%n",
                        o.getId(),
                        escapeCsv(o.getTitle()),
                        escapeCsv(o.getDescription()),
                        escapeCsv(o.getRequirements()),
                        escapeCsv(o.getArea()),
                        o.getStartDate(),
                        o.getEndDate(),
                        o.getVacancies(),
                        o.isOffer(),
                        o.getCompany() != null ? o.getCompany().getId() : "");
            }
        }
    }

    // Util method to set headers for CSV response
    private void setCSVHeaders(HttpServletResponse response, String fileName) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    }

    // Escape commas, quotes, and newlines in CSV fields
    private String escapeCsv(String value) {
        if (value == null)
            return "";
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    // Not used - but kept for reference
    @GetMapping("/candidates/json")
    public void exportCandidatesToJson(HttpServletResponse response)
            throws IOException, StreamWriteException, DatabindException, java.io.IOException {
        List<Candidate> candidates = candidateService.getAllCandidates();

        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=candidates.json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), candidates);
    }
}
