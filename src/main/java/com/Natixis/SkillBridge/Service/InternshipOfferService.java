package com.Natixis.SkillBridge.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.Natixis.SkillBridge.Repository.ApplicationRepository;
import com.Natixis.SkillBridge.Repository.InternshipOfferRepository;
import com.Natixis.SkillBridge.model.Application;
import com.Natixis.SkillBridge.model.InternshipOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class InternshipOfferService {
    Logger logger = LoggerFactory.getLogger(InternshipOfferService.class);

    @Autowired
    private InternshipOfferRepository repository;

    @Autowired
    private ApplicationRepository applicationRepository;

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

        InternshipOffer existingOffer = repository.findById(id)

                .orElseThrow(() -> new IllegalArgumentException("InternshipOffer not found with id " + id));
        logger.info("Attempting to update InternshipOffer with ID: {}", id);
        existingOffer.setTitle(offerDetails.getTitle());
        existingOffer.setDescription(offerDetails.getDescription());
        existingOffer.setRequirements(offerDetails.getRequirements());
        existingOffer.setArea(offerDetails.getArea());
        existingOffer.setStartDate(offerDetails.getStartDate());
        existingOffer.setEndDate(offerDetails.getEndDate());
        existingOffer.setVacancies(offerDetails.getVacancies());
        existingOffer.setCompany(offerDetails.getCompany());
        existingOffer.setOffer(offerDetails.isOffer());

    // Ensures the applications list is never null
        List<Application> newApplications = Optional.ofNullable(offerDetails.getApplications())
                .orElse(Collections.emptyList());

        List<Application> existingApplications = Optional.ofNullable(existingOffer.getApplications())
                .orElseGet(() -> {
                    List<Application> emptyList = new ArrayList<>();
                    existingOffer.setApplications(emptyList);
                    return emptyList;
                });

    // Removes applications that are no longer in the new list
        existingApplications.removeIf(app -> !newApplications.contains(app));

    // Adds new applications that are not yet in the existing list
        for (Application newApp : newApplications) {
            if (!existingApplications.contains(newApp)) {
                existingApplications.add(newApp);
                newApp.setInternshipOffer(existingOffer);
            }
        }

        return repository.save(existingOffer);
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

    public List<InternshipOffer> getTopOffersByApplications(int limit) {
        List<Object[]> results = applicationRepository.findTopOffersByApplications(PageRequest.of(0, limit));
        List<Long> offerIds = results.stream()
                .map(r -> (Long) r[0])
                .toList();
        return repository.findAllById(offerIds);
    }

    // Find InternshipOffers by Company ID
    public List<InternshipOffer> findByCompanyId(Long companyId) {
        logger.info("Attempting to find InternshipOffers for Company ID: {}", companyId);
        return repository.findByCompanyId(companyId);
    }

    public InternshipOffer changeStatus(Long offerId, boolean status) {
        logger.info("Attempting to change status of InternshipOffer with ID: {}", offerId);
        Optional<InternshipOffer> offerOpt = repository.findById(offerId);
        if (offerOpt.isPresent()) {
            InternshipOffer offer = offerOpt.get();
            offer.setOffer(status);
            return repository.save(offer);
        }
        return null;
    }

}