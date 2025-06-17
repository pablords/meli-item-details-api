package com.pablords.spring_template.mapper;

import com.pablords.spring_template.dto.UserRequestDto;
import com.pablords.spring_template.dto.UserResponseDto;
import com.pablords.spring_template.model.User;

public class UserMapper {
  public static UserResponseDto toResponseDto(User user) {
    if (user == null) {
      return null;
    }
    UserResponseDto responseDto = new UserResponseDto();
    responseDto.setId(user.getId());
    responseDto.setName(user.getName());
    responseDto.setEmail(user.getEmail());
    return responseDto;
  }

  public static User toEntity(UserRequestDto userRequestDto) {
    if (userRequestDto == null) {
      return null;
    }
    User user = new User();
    user.setName(userRequestDto.getName());
    user.setEmail(userRequestDto.getEmail());
    return user;
  }
}
