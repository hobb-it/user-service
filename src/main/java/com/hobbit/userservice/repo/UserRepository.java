package com.hobbit.userservice.repo;

import com.hobbit.userservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

}