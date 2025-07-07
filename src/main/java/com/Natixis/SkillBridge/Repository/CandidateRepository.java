package com.Natixis.SkillBridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.user.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
   
   
}
