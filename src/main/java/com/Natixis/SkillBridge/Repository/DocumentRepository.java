package com.Natixis.SkillBridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Natixis.SkillBridge.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}