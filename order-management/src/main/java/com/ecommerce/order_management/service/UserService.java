package com.ecommerce.order_management.service;

import com.ecommerce.order_management.entity.User;
import com.ecommerce.order_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User registerUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already registered: "+ user.getEmail());
        }
        if(user.getUserType() == null || user.getUserType().isEmpty()){
            user.setUserType("REGULAR");
        }

        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found with id: "+id));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found with Email: "+email));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
