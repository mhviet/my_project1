package com.viet.bookmanagement.repositoties;

import com.viet.bookmanagement.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MyUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
