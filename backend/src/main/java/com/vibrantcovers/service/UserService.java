package com.vibrantcovers.service;

import com.vibrantcovers.entity.User;
import com.vibrantcovers.repository.UserRepository;
import com.vibrantcovers.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public User createOrGetUser(String userId, String email) {
        return userRepository.findById(userId)
                .orElseGet(() -> {
                    User user = new User();
                    user.setId(userId);
                    user.setEmail(email);
                    return userRepository.save(user);
                });
    }
    
    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

