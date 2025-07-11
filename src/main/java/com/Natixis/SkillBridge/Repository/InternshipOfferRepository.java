package com.Natixis.SkillBridge.Repository;

import com.Natixis.SkillBridge.model.InternshipOffer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipOfferRepository extends JpaRepository<InternshipOffer, Long> {
    List<InternshipOffer> findByCompanyId(Long companyId);
}
