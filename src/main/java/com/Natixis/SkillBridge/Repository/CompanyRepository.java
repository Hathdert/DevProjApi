package com.Natixis.SkillBridge.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.utilizador.Company;
import com.Natixis.SkillBridge.model.utilizador.User;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findCompanyById(Long id);
    
}
