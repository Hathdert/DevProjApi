package com.Natixis.SkillBridge.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Natixis.SkillBridge.model.utilizador.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    
    // Exemplo: Buscar por e-mail (caso sua entidade tenha esse campo)
    // Optional<Candidate> findByEmail(String email);

}