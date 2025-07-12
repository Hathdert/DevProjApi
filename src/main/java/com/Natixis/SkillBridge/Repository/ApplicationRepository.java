package com.Natixis.SkillBridge.Repository;

import java.util.List;
import org.springframework.data.domain.Pageable;  // IMPORT CORRETO!

import org.springframework.data.domain.Pageable; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Natixis.SkillBridge.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    // List all applications inside an internship offer
    List<Application> findAllByInternshipOfferId(Long internshipOfferId);

    // List all applications by candidate ID
    List<Application> findAllByCandidateId(Long candidateId);

    // Query para top ofertas por número de candidaturas
    @Query("SELECT a.internshipOffer.id, COUNT(a.id) as totalApps " +
           "FROM Application a " +
           "GROUP BY a.internshipOffer.id " +
           "ORDER BY totalApps DESC")
    List<Object[]> findTopOffersByApplications(Pageable pageable);

    // Query para top empresas por número de candidaturas em suas ofertas
    @Query("SELECT a.internshipOffer.company.id, COUNT(a.id) as totalApps " +
           "FROM Application a " +
           "GROUP BY a.internshipOffer.company.id " +
           "ORDER BY totalApps DESC")
    List<Object[]> findTopCompaniesByApplications(Pageable pageable);
}
