package com.Natixis.SkillBridge.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.user.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
   
      Optional<Candidate> findByEmail(String email);
}
