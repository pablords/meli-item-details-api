package com.pablords.spring_template.controller;

import com.pablords.spring_template.dto.UserRequestDto;
import com.pablords.spring_template.dto.UserResponseDto;
import com.pablords.spring_template.mapper.UserMapper;
import com.pablords.spring_template.model.User;
import com.pablords.spring_template.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController implements UserSwagger {

  @Autowired
  private UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto create(@RequestBody UserRequestDto user) {
    log.info("Creating user: {}", user.toString());
    var userEntity = UserMapper.toEntity(user);
    User createdUser = userService.registerUser(userEntity);
    log.info("User created successfully: {}", createdUser.toString());
    return UserMapper.toResponseDto(createdUser);
  }
}