package com.Natixis.SkillBridge.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Natixis.SkillBridge.Repository.ApplicationRepository;
import com.Natixis.SkillBridge.Repository.CompanyRepository;
import com.Natixis.SkillBridge.model.user.Candidate;
import com.Natixis.SkillBridge.model.user.Company;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // Get all companies
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // Get the company by ID
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    // Update the company by ID
    public Company updateCompany(Long id, Company updatedCompany) {
        Company existing = getCompanyById(id);
        System.out.println("idcandidate " + getCompanyById(id));
        existing.setName(updatedCompany.getName());
        existing.setEmail(updatedCompany.getEmail());
        existing.setPhone(updatedCompany.getPhone());
        existing.setAddress(updatedCompany.getAddress());
        existing.setDescription(updatedCompany.getDescription());
        existing.setNipc(updatedCompany.getNipc());
        existing.setArea(updatedCompany.getArea());

        return companyRepository.save(existing);
    }

    // Delete the company by ID
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Company not found");
        }
        companyRepository.deleteById(id);
    }

    public Long getCompanyIdByEmail(String email) {
        return companyRepository.findByEmail(email)
                .map(Company::getId)
                .orElseThrow(() -> new RuntimeException("Company not found with email: " + email));
    }

    public Company updateApprovalStatus(Long companyId, int newStatus) {
        Company company = getCompanyById(companyId);
        if (company == null) {
            throw new RuntimeException("Company not found");
        }
        company.setApprovalStatus(newStatus);
        return companyRepository.save(company);
    }

    public List<Company> getTopCompaniesByApplications(int limit) {
    List<Object[]> results = applicationRepository.findTopCompaniesByApplications(PageRequest.of(0, limit));
    List<Long> companyIds = results.stream()
                                  .map(r -> (Long) r[0])
                                  .toList();
    return companyRepository.findAllById(companyIds);
}
}
