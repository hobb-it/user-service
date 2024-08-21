package com.hobbit.userservice.controller;

import com.hobbit.userservice.model.*;
import com.hobbit.userservice.repo.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setEmail(user.getEmail());
        newUser.setCellNum(user.getCellNum());
        newUser.setUsername(user.getUsername());

        Optional<User> oldUser = userRepository.findUserByEmail(newUser.getEmail());
        if (oldUser.isEmpty()) {
            String emailAndName = newUser.getEmail() + "," + newUser.getName();
            rabbitTemplate.convertAndSend("notificationService", emailAndName);
            return userRepository.save(newUser);
        }
        return oldUser.get();
    }

    @GetMapping("/all")
    public Iterable<User> getUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @GetMapping("/contacts")
    public Map<String, String> getContactsOfUsers(
            @RequestParam(value="users") String users
    ) {
        String[] userList = users.split(" ");
        List<User> userContacts = userRepository.findEmailByUsernameIn(userList);
        Map<String, String> userContactsMap = new HashMap<>();
        for (User user : userContacts) {
            userContactsMap.put(user.getUsername(), user.getEmail());
        }
        return userContactsMap;
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
            userRepository.save(_user);
        }
    }

}
