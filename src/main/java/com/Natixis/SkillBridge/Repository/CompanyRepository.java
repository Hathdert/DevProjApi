package com.Natixis.SkillBridge.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.user.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findCompanyById(Long id);
    Optional<Company> findByEmail(String email);
}
