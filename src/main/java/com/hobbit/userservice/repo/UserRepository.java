package com.hobbit.userservice.repo;

import com.hobbit.userservice.model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    List<User> findEmailByUsernameIn(String[] username);
    Optional<User> findUserByEmail(String email);
}