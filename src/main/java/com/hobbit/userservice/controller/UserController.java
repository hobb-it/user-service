package com.hobbit.userservice.controller;

import com.hobbit.userservice.model.*;
import com.hobbit.userservice.repo.*;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setEmail(user.getEmail());
        newUser.setCellNum(user.getCellNum());
        newUser.setPassword(user.getPassword());

        return userRepository.save(newUser);
    }

    @GetMapping("/all")
    public Iterable<User> getUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable String id) {
        UUID userId = UUID.fromString(id);
        return userRepository.findById(userId);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        UUID userId = UUID.fromString(id);
        userRepository.deleteById(userId);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody User user) {
        UUID userId = user.getId();
        Optional<User> userData = userRepository.findById(userId);
        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setName(user.getName());
            _user.setSurname(user.getSurname());
            _user.setEmail(user.getEmail());
            _user.setCellNum(user.getCellNum());
            _user.setPassword(user.getPassword());
            userRepository.save(_user);
        }
    }

}
