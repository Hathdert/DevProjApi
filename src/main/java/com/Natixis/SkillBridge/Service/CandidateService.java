package com.Natixis.SkillBridge.Service;

import org.springframework.stereotype.Service;

import com.Natixis.SkillBridge.Repository.CandidateRepository;
import com.Natixis.SkillBridge.model.utilizador.Candidate;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    // Injeção por construtor
    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public Candidate salvarCandidato(Candidate candidate) {
        return candidateRepository.save(candidate);
    }
}
