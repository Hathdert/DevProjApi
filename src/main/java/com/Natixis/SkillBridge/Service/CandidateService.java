package com.Natixis.SkillBridge.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Natixis.SkillBridge.Repository.CandidateRepository;
import com.Natixis.SkillBridge.model.user.Candidate;

@Service
public class CandidateService {
    private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);

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
        if (existing == null) {
            logger.error("Candidate not found with id: {}", id);
            throw new RuntimeException("Candidate not found");
        }
        existing.setName(updatedCandidate.getName());
        existing.setEmail(updatedCandidate.getEmail());
        existing.setPhone(updatedCandidate.getPhone());
        existing.setAddress(updatedCandidate.getAddress());

        return candidateRepository.save(existing);
    }

    // Method to delete a candidate
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            logger.error("Candidate not found with id: {}", id);
            throw new RuntimeException("Candidate not found");
        }
        candidateRepository.deleteById(id);
    }

    public Long getCandidateIdByEmail(String email) {
        return candidateRepository.findByEmail(email)
                .map(candidate -> {
                    logger.info("Candidate found with email: {}", email);
                    return candidate.getId();
                })
                .orElseThrow(() -> {
                    logger.warn("Candidate not found with email: {}", email);
                    return new RuntimeException("Candidate not found with email: " + email);
                });
    }

}
