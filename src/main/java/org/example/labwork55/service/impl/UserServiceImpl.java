package org.example.labwork55.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dao.UserDao;
import org.example.labwork55.dto.UserDto;
import org.example.labwork55.exceptions.UserNotFoundException;
import org.example.labwork55.model.User;
import org.example.labwork55.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userDao.getUsers();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(int id) {
        User user = userDao.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapToDto(user);
    }

    @Override
    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
        user.setRoleId(1);

        userDao.create(user);
    }

    @Override
    public int createUserAndReturnId(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
        user.setRoleId(1);

        return userDao.createAndReturnId(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return mapToDto(user);
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}