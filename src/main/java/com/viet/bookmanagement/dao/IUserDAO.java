package com.viet.bookmanagement.dao;

import com.viet.bookmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserDAO extends JpaRepository<User, Long> {

    static final  String GET_USER_BY_USERNAME = "SELECT * FROM users WHERE username =:username";


    @Query(value = GET_USER_BY_USERNAME, nativeQuery = true)
    public User findByUsername(@Param("username") String username);

    User findByEmail(String email);
//    List<User> findAll();
}
