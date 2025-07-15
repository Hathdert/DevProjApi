package com.Natixis.SkillBridge.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Natixis.SkillBridge.Repository.ApplicationRepository;
import com.Natixis.SkillBridge.model.Application;

@Service
public class ApplicationService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    
    @Autowired
    private ApplicationRepository applicationRepository;

    // List all applications
    public List<Application> listAll() {
        return applicationRepository.findAll();
    }

    // Search application by ID
    public Application findById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    // List all applications by candidate ID
    public List<Application> listByCandidateId(Long candidateId) {
        return applicationRepository.findAllByCandidateId(candidateId);
    }

    // List all applications inside an internship offer
    public List<Application> listByInternshipOfferId(Long internshipOfferId) {
        return applicationRepository.findAllByInternshipOfferId(internshipOfferId);
    }

    // Creaate new application
    public Application makeNew(Application application) {
        return applicationRepository.save(application);
    }

    // Update application
    public Application update(Long id, Application application) {
        
        Application existingApplication = findById(id);
        
        if (existingApplication != null) {
            existingApplication.setCandidate(application.getCandidate());
            existingApplication.setDocument(application.getDocument());
            existingApplication.setPitch(application.getPitch());
            existingApplication.setState(application.getState());
            existingApplication.setInternshipOffer(application.getInternshipOffer());
            return applicationRepository.save(existingApplication);
        }
        logger.error("Application with ID {} not found for update", id);
        return null;
    }

    // Delete application
    public boolean delete(Long id) {
        Application existingApplication = findById(id);
        
        if (existingApplication != null) {
            applicationRepository.delete(existingApplication);
            return true;
        }
        logger.error("Application with ID {} not found for deletion", id);
        return false;
    }

    // Delete application by the candidate id
    public boolean deleteByCandidateId(Long id) {
        List<Application> existingApplicationList = applicationRepository.findAllByCandidateId(id);

        if (existingApplicationList.size() != 0) {
            for (Application application : existingApplicationList) {
                applicationRepository.delete(application);
                return true;
            }
        }
        logger.error("No applications found for candidate ID {}", id);
        return false;
    }


    public Application changeStatus(Long applicationId, int status) {
        Application application = findById(applicationId);
        if (application != null) {
            application.setState(status);
            return applicationRepository.save(application);
        }
        logger.error("Application with ID {} not found for status change", applicationId);
        return null;
    }
}
