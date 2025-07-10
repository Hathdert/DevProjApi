package com.Natixis.SkillBridge.Service;

import com.Natixis.SkillBridge.Repository.InternshipOfferRepository;
import com.Natixis.SkillBridge.model.Application;
import com.Natixis.SkillBridge.model.InternshipOffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternshipOfferService {

    @Autowired
    private InternshipOfferRepository repository;

    // List all InternshipOffer
    public List<InternshipOffer> findAll() {
        return repository.findAll();
    }

    // Search InternshipOffer by ID
    public Optional<InternshipOffer> findById(Long id) {
        return repository.findById(id);
    }

    // Save new InternshipOffer
    public InternshipOffer save(InternshipOffer offer) {
        return repository.save(offer);
    }

    // Update InternshipOffer
    public InternshipOffer update(Long id, InternshipOffer offerDetails) {
        Optional<InternshipOffer> existingOfferOpt = repository.findById(id);

        if (existingOfferOpt.isPresent()) {
            InternshipOffer existingOffer = existingOfferOpt.get();

            existingOffer.setTitle(offerDetails.getTitle());
            existingOffer.setDescription(offerDetails.getDescription());
            existingOffer.setRequirements(offerDetails.getRequirements());
            existingOffer.setArea(offerDetails.getArea());
            existingOffer.setStartDate(offerDetails.getStartDate());
            existingOffer.setEndDate(offerDetails.getEndDate());
            existingOffer.setVacancies(offerDetails.getVacancies());
            existingOffer.setCompany(offerDetails.getCompany());
            existingOffer.setOffer(offerDetails.isOffer());
            existingOffer.setApplications(offerDetails.getApplications());

            return repository.save(existingOffer);
        } else {
            return null;
        }
    }

    // Delete InternshipOffer
    public boolean delete(Long id) {
        Optional<InternshipOffer> existingOfferOpt = repository.findById(id);
        if (existingOfferOpt.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // Check if there are applications in the offer and if any of them are pendent
    public boolean checkPendentApplications(InternshipOffer internshipOffer) {
        List<Application> applications = internshipOffer.getApplications();

        if (applications.size() > 0) {
            for (Application application : applications) {
                if (application.getState() == 0) {
                    return false;
                }
            }
        }
        
        return true;
    } 
}
