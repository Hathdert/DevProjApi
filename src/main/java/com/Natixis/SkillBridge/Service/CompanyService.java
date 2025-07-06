package com.Natixis.SkillBridge.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Natixis.SkillBridge.Repository.CompanyRepository;
import com.Natixis.SkillBridge.model.utilizador.Company;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;



    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    //Get all companies
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    //Get the company by ID
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        Company existing = getCompanyById(id);
        System.out.println("idcandidate " + getCompanyById(id));
        existing.setName(updatedCompany.getName());
        existing.setEmail(updatedCompany.getEmail());
        existing.setPhone(updatedCompany.getPhone());
        existing.setAddress(updatedCompany.getAddress());

        return companyRepository.save(existing);
    }

    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Company not found");
        }
        companyRepository.deleteById(id);
    }

}
