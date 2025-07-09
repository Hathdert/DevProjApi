package com.Natixis.SkillBridge.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Natixis.SkillBridge.Repository.CandidateRepository;
import com.Natixis.SkillBridge.model.user.Candidate;

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
                .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + id));
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
            throw new RuntimeException("Candidate not found");
        }
        candidateRepository.deleteById(id);
    }

    public Long getCandidateIdByEmail(String email) {
    return candidateRepository.findByEmail(email)
        .map(Candidate::getId)
        .orElseThrow(() -> new RuntimeException("Candidate not found with email: " + email));
}


}
