package com.Natixis.SkillBridge.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    // List all applications inside an internship offer
    List<Application> findAllByInternshipOfferId(Long internshipOfferId);

    // List all applications by candidate ID
    List<Application> findAllByCandidateId(Long candidateId);
}
