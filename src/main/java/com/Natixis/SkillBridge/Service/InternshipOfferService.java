package com.Natixis.SkillBridge.Service;

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

    @Autowired
    private InternshipOfferRepository repository;

    @Autowired
    private ApplicationRepository applicationRepository;

    // List all InternshipOffer
    public List<InternshipOffer> findAll() {
        return repository.findAll();
    }

    // Search InternshipOffer by ID
    public InternshipOffer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("InternshipOffer not found with id: " + id));
    }

    // Save new InternshipOffer
    public InternshipOffer save(InternshipOffer offer) {
        return repository.save(offer);
    }

    // Update InternshipOffer
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
}
