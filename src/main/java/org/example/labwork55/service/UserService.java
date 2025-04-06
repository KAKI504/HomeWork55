package org.example.labwork55.service;

import org.example.labwork55.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
    UserDto getUserById(int id);
    void registerUser(UserDto userDto);
    int createUserAndReturnId(UserDto userDto);
    UserDto getUserByEmail(String email);
}