package com.viet.bookmanagement.services.impl;

import com.viet.bookmanagement.dao.IBorrowingDAO;
import com.viet.bookmanagement.dao.IUserDAO;
import com.viet.bookmanagement.entities.Borrowing;
import com.viet.bookmanagement.entities.User;
import com.viet.bookmanagement.exception.UserNotFoundException;
import com.viet.bookmanagement.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IBorrowingDAO borrowingDAO;


    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public Page<User> findAllUser(Pageable pageable) {
        return userDAO.findAll(pageable);
    }

    public void registerUser(User user) {
        userDAO.save(user);
    }

    @Override
    public boolean checkUserExists(User user){
        if (checkUsernameExists(user.getUsername()) || checkEmailExists(user.getEmail())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }

        return false;
    }

    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        }

        return false;
    }


    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userDAO.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setEmail(updatedUser.getEmail());

        return userDAO.save(existingUser);
    }

    public void deleteUser(Long userId) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userDAO.delete(user);
    }

    @Override
    public boolean checkUserBorrowing(Long userId) {
        List<Borrowing> borrowings = borrowingDAO.findByUserId(userId);

        return borrowings != null && borrowings.size() > 0;
    }
}
