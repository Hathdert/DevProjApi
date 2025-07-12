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
        logger.info("Attempting to update InternshipOffer with ID: {}", id);
public InternshipOffer update(Long id, InternshipOffer offerDetails) {
    InternshipOffer existingOffer = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("InternshipOffer not found with id " + id));

    existingOffer.setTitle(offerDetails.getTitle());
    existingOffer.setDescription(offerDetails.getDescription());
    existingOffer.setRequirements(offerDetails.getRequirements());
    existingOffer.setArea(offerDetails.getArea());
    existingOffer.setStartDate(offerDetails.getStartDate());
    existingOffer.setEndDate(offerDetails.getEndDate());
    existingOffer.setVacancies(offerDetails.getVacancies());
    existingOffer.setCompany(offerDetails.getCompany());
    existingOffer.setOffer(offerDetails.isOffer());

    // Garante que a lista de applications nunca seja nula
    List<Application> newApplications = Optional.ofNullable(offerDetails.getApplications())
                                                 .orElse(Collections.emptyList());

    List<Application> existingApplications = Optional.ofNullable(existingOffer.getApplications())
                                                     .orElseGet(() -> {
                                                         List<Application> emptyList = new ArrayList<>();
                                                         existingOffer.setApplications(emptyList);
                                                         return emptyList;
                                                     });

    // Remove as applications que não estão mais na nova lista
    existingApplications.removeIf(app -> !newApplications.contains(app));

    // Adiciona as novas applications que ainda não estão na lista existente
    for (Application newApp : newApplications) {
        if (!existingApplications.contains(newApp)) {
            existingApplications.add(newApp);
            newApp.setInternshipOffer(existingOffer);  // garante relacionamento bidirecional
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

    // Find InternshipOffers by Company ID
    public List<InternshipOffer> findByCompanyId(Long companyId) {
        return repository.findByCompanyId(companyId);
    }
}
}
