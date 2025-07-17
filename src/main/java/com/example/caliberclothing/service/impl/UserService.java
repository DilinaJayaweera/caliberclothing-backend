package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.User;
import com.example.caliberclothing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndIsActiveTrue(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found or inactive: " + username);
        }
        return new CustomUserDetails(user.get());
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username);
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deactivateUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setIsActive(false);
            userRepository.save(user.get());
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}

