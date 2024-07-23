package com.hobbit.userservice.repo;

import com.hobbit.userservice.model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

}