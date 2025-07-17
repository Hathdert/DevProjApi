package com.Natixis.SkillBridge.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Natixis.SkillBridge.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByCandidateId(Long candidateId);

    List<Document> findByCompanyId(Long companyId);

    List<Document> findByApplicationId(Long applicationId);
}