package com.Natixis.SkillBridge.Service;

import com.Natixis.SkillBridge.Repository.InternshipOfferRepository;
import com.Natixis.SkillBridge.model.InternshipOffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternshipOfferService {

    @Autowired
    private InternshipOfferRepository repository;

    // Listar todos
    public List<InternshipOffer> findAll() {
        return repository.findAll();
    }

    // Buscar por ID
    public Optional<InternshipOffer> findById(Long id) {
        return repository.findById(id);
    }

    // Salvar nova oferta
    public InternshipOffer save(InternshipOffer offer) {
        return repository.save(offer);
    }

    // Atualizar oferta
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
            // Aqui pode lançar exceção ou retornar null
            return null;
        }
    }

    // Deletar oferta
    public boolean delete(Long id) {
        Optional<InternshipOffer> existingOfferOpt = repository.findById(id);
        if (existingOfferOpt.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
