package com.Natixis.SkillBridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Natixis.SkillBridge.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
}
