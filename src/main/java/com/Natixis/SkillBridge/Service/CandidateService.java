package com.Natixis.SkillBridge.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Natixis.SkillBridge.Repository.CandidateRepository;
import com.Natixis.SkillBridge.model.utilizador.Candidate;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    // Constructor injection for CandidateRepository
    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }
    // Method to register a new candidate
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }
    // Method to get a candidate by ID
    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado com ID: " + id));
    }
    // Method to update a candidate
    public Candidate updateCandidate(Long id, Candidate updatedCandidate) {
        Candidate existing = getCandidateById(id);
        System.out.println("idcandidate " + getCandidateById(id));
        existing.setName(updatedCandidate.getName());
        existing.setEmail(updatedCandidate.getEmail());
        existing.setPhone(updatedCandidate.getPhone());
        existing.setAddress(updatedCandidate.getAddress());

        return candidateRepository.save(existing);
    }
    // Method to delete a candidate
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new RuntimeException("Candidato não encontrado");
        }
        candidateRepository.deleteById(id);
    }

}
