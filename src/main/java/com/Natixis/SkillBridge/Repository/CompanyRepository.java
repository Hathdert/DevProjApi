package com.Natixis.SkillBridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.utilizador.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    
}
