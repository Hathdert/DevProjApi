package com.Natixis.SkillBridge.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Natixis.SkillBridge.Repository.ApplicationRepository;
import com.Natixis.SkillBridge.model.Application;
import com.Natixis.SkillBridge.model.InternshipOffer;

@Service
public class ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;

    // List all applications
    public List<Application> listAll() {
        return applicationRepository.findAll();
    }

    // Search application by ID
    public Application findById(Long id) {
        return applicationRepository.findById(id).orElse(null);
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
            return applicationRepository.save(existingApplication);
        }

        return null;
    }

    // Delete application
    public boolean delete(Long id) {
        Application existingApplication = findById(id);
        
        if (existingApplication != null) {
            applicationRepository.delete(existingApplication);
            return true;
        }
        
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

        return false;
    }

    // // Check if there are applications in the offer and if any of them are pendent
    // public boolean checkPendentApplications(InternshipOffer internshipOffer) {
    //     List<Application> applications = internshipOffer.getApplications();

    //     if (applications.size() > 0) {
    //         for (Application application : applications) {
    //             if (application.getState() == 0) {
    //                 return false;
    //             }
    //         }
    //     }
        
    //     return true;

    // }
}
