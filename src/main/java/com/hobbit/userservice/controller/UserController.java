package com.hobbit.userservice.controller;

import com.hobbit.userservice.model.*;
import com.hobbit.userservice.repo.*;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @PostMapping(value = "/user/create")
    @ResponseStatus(HttpStatus.CREATED)
    public User postUser(@RequestBody User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setEmail(user.getEmail());
        newUser.setCellNum(user.getCellNum());
        newUser.setPassword(user.getPassword());

        return userRepository.save(newUser);
    }

    @GetMapping(value = "/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        final ArrayList users = new ArrayList<>((Collection) userRepository.findAll());
        return users;
    }
}
