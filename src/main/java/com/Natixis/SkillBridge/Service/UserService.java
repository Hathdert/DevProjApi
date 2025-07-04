package com.Natixis.SkillBridge.Service;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.stereotype.Service;
import com.Natixis.SkillBridge.Repository.UserRepository;
import com.Natixis.SkillBridge.model.utilizador.User;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        
     
        return userRepository.save(user); // Assuming userRepository has a save method
    }
   

   
}
