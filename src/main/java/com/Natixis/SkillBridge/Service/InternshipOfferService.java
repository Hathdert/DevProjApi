package com.Natixis.SkillBridge.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Natixis.SkillBridge.Repository.InternshipOfferRepository;
import com.Natixis.SkillBridge.model.InternshipOffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternshipOfferService {
    Logger logger = LoggerFactory.getLogger(InternshipOfferService.class);

    @Autowired
    private InternshipOfferRepository repository;

    // List all InternshipOffer
    public List<InternshipOffer> findAll() {
        logger.info("Attempting to retrieve all InternshipOffers");
        return repository.findAll();
    }

    // Search InternshipOffer by ID
    public Optional<InternshipOffer> findById(Long id) {
        logger.info("Attempting to find InternshipOffer with ID: {}", id);
        return repository.findById(id);
    }

    // Save new InternshipOffer
    public InternshipOffer save(InternshipOffer offer) {
        logger.info("Attempting to save a new InternshipOffer: {}", offer.getTitle());
        return repository.save(offer);
    }

    // Update InternshipOffer
    public InternshipOffer update(Long id, InternshipOffer offerDetails) {
        logger.info("Attempting to update InternshipOffer with ID: {}", id);
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

            logger.info("Updating InternshipOffer with ID: {}", id);
            return repository.save(existingOffer);
        } else {
            logger.warn("InternshipOffer with ID: {} not found for update", id);
            return null;
        }
    }

    // Delete InternshipOffer
    public boolean delete(Long id) {
        logger.info("Attempting to delete InternshipOffer with ID: {}", id);
        Optional<InternshipOffer> existingOfferOpt = repository.findById(id);
        if (existingOfferOpt.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
