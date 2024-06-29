package com.viet.bookmanagement.services;

import com.viet.bookmanagement.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    public User findByUsername(String username);
    public User findByEmail(String email);


    public Page<User> findAllUser(Pageable pageable);
    public void registerUser(User user);

    boolean checkUserExists(User user);
    public User updateUser(Long userId, User updatedUser);
    public void deleteUser(Long userId);

    boolean checkUserBorrowing(Long userId);
}
