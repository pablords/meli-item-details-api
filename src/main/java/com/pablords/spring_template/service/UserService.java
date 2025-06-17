package com.pablords.spring_template.service;

import com.pablords.spring_template.model.User;
import com.pablords.spring_template.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User registerUser(User user) {
    log.info("Registering user with email: {}", user.getEmail());
    if (this.getUserByEmail(user.getEmail()).isPresent()) {
      log.error("A email already exists: {}", user.getEmail());
      throw new IllegalArgumentException("A email already exists: " + user.getEmail());
    }
    log.info("User registered successfully: {}", user.getEmail());
    return userRepository.save(user);
  }

  private Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}