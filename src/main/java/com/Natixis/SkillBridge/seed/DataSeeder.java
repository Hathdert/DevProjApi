package com.Natixis.SkillBridge.seed;


import com.Natixis.SkillBridge.Repository.CandidateRepository;
import com.Natixis.SkillBridge.Repository.CompanyRepository;
import com.Natixis.SkillBridge.Service.ApplicationService;
import com.Natixis.SkillBridge.Service.CandidateService;
import com.Natixis.SkillBridge.Service.CompanyService;
import com.Natixis.SkillBridge.Service.DocumentService;
import com.Natixis.SkillBridge.Service.InternshipOfferService;
import com.Natixis.SkillBridge.model.Application;
import com.Natixis.SkillBridge.model.InternshipOffer;
import com.Natixis.SkillBridge.model.user.Candidate;
import com.Natixis.SkillBridge.model.user.Company;
import com.Natixis.SkillBridge.util.InMemoryMultipartFile;
import com.github.javafaker.Faker;

import java.io.IOException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;


@Component
public class DataSeeder {
    @Autowired private CompanyService companyService;
    @Autowired private CandidateService candidateService;
    @Autowired private InternshipOfferService internshipOfferService;
    @Autowired private ApplicationService applicationService;
    @Autowired private DocumentService documentService;
    @Autowired private CandidateRepository candidateRepository;
    @Autowired private CompanyRepository companyRepository;

    private final Faker faker = new Faker();

    @PostConstruct
    public void seed() {
        seedCompanies(50);
        seedCandidates(100);
        seedOffers(80);
        seedApplications(200);
    }

    private void seedCompanies(int count) {
        for (int i = 0; i < count; i++) {
            var company = new Company();
            company.setName(faker.company().name());
            company.setEmail(faker.internet().emailAddress());
            company.setPassword("pass123");
            company.setAddress(faker.address().fullAddress());
            company.setPhone(faker.phoneNumber().cellPhone());
            company.setArea(faker.company().industry());
            company.setNipc(faker.number().numberBetween(100000000, 999999999));
            company.setRole("COMPANY");
            company.setApprovalStatus(faker.number().numberBetween(0, 2));
            company.setRegistrationDate(LocalDate.now());
            company.setRegistrationTime(LocalTime.now());

            company.setDescription(faker.company().catchPhrase());

            // Exactly 9-digit phone number
            company.setPhone(String.valueOf(faker.number().numberBetween(900000000, 999999999)));

    
            companyRepository.save(company);
    
            // Attach 1–2 fake documents
            int docCount = faker.number().numberBetween(1, 3);
            for (int d = 0; d < docCount; d++) {
                try {
                    String fileName = faker.file().fileName(null, null, "pdf", null);
                    byte[] content = ("Fake file for company " + company.getEmail()).getBytes(StandardCharsets.UTF_8);
                    MultipartFile file = new InMemoryMultipartFile(
                        "file", fileName, "application/pdf", content
                    );
    
                    documentService.saveDocumentCompany(file, company);
    
                } catch (IOException e) {
                    System.err.println("Failed to attach document to company: " + company.getEmail());
                    e.printStackTrace();
                }
            }
        }
    }
    

    private void seedCandidates(int count) {
        for (int i = 0; i < count; i++) {
            var candidate = new Candidate();
            candidate.setName(faker.name().fullName());
            candidate.setEmail(faker.internet().emailAddress());
            candidate.setPassword("pass123");
            candidate.setAddress(faker.address().fullAddress());
            candidate.setPhone(String.valueOf(faker.number().numberBetween(900000000, 999999999)));
            candidate.setRole("CANDIDATE");
            candidate.setBirthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            candidate.setRegistrationDate(LocalDate.now().minusDays(faker.number().numberBetween(0, 365)));
            candidate.setRegistrationTime(LocalTime.now());

            candidateRepository.save(candidate);

            // Add 1 to 3 documents for this candidate
            int docCount = faker.number().numberBetween(1, 4);
            for (int d = 0; d < docCount; d++) {
                try {
                    String fileName = faker.file().fileName(null, null, "pdf", null);
                    byte[] content = ("Fake CV for " + candidate.getEmail()).getBytes(StandardCharsets.UTF_8);
                    MultipartFile file = new InMemoryMultipartFile(
                        "file", fileName, "application/pdf", content
                    );

                    documentService.saveDocumentCandidate(file, candidate);

                } catch (IOException e) {
                    System.err.println("Failed to create document for " + candidate.getEmail());
                    e.printStackTrace();
                }
            }
        }
    }


    private void seedOffers(int count) {
        List<Company> companies = companyService.getAllCompanies();
        for (int i = 0; i < count; i++) {
            var offer = new InternshipOffer();
            var company = faker.options().nextElement(companies);

            offer.setTitle(faker.job().title());
            offer.setDescription(faker.lorem().sentence(20));
            offer.setRequirements(faker.lorem().sentence(12));
            offer.setArea(faker.company().industry());
            offer.setCompany(company);
            offer.setVacancies((faker.number().numberBetween(10, 50)));
            offer.setStartDate(LocalDate.now().plusDays(faker.number().numberBetween(30, 90)));
            offer.setEndDate(offer.getStartDate().plusMonths(3));

            internshipOfferService.save(offer);
        }
    }
    
    private void seedApplications(int count) {
        List<Candidate> candidates = candidateService.getAllCandidates();
        List<InternshipOffer> offers = internshipOfferService.findAll();
    
        for (int i = 0; i < count; i++) {
            var app = new Application();
            app.setCandidate(faker.options().nextElement(candidates));
            app.setPitch(faker.lorem().sentence(20));
            app.setInternshipOffer(faker.options().nextElement(offers));
            app.setState(faker.number().numberBetween(0, 3));
    
            app = applicationService.makeNew(app); // reassign to get saved ID
    
            // Attach 1 fake document
            try {
                String fileName = faker.file().fileName(null, null, "pdf", null);
                byte[] content = ("Fake file for application " + app.getId()).getBytes(StandardCharsets.UTF_8);
                MultipartFile file = new InMemoryMultipartFile(
                    "file", fileName, "application/pdf", content
                );
    
                documentService.saveDocumentApplication(file, app);
    
            } catch (IOException e) {
                System.err.println("Failed to attach document to application");
                e.printStackTrace();
            }
        }
    }
    
    
}
