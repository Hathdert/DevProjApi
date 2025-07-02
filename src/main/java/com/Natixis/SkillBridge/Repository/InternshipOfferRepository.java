package com.Natixis.SkillBridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.InternshipOffer;

public interface InternshipOfferRepository extends JpaRepository<InternshipOffer, Long>  {
    
}
