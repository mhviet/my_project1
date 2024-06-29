package com.viet.bookmanagement.controller;

import com.viet.bookmanagement.Utils.Utils;
import com.viet.bookmanagement.dtos.BorrowingDTO;
import com.viet.bookmanagement.dtos.UserBorrowingDTO;
import com.viet.bookmanagement.entities.Borrowing;
import com.viet.bookmanagement.entities.User;
import com.viet.bookmanagement.exception.InputValidationException;
import com.viet.bookmanagement.exception.UserException;
import com.viet.bookmanagement.exception.UserExistException;
import com.viet.bookmanagement.exception.UserNotFoundException;
import com.viet.bookmanagement.services.IBorrowingService;
import com.viet.bookmanagement.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IBorrowingService borrowingService;

    //?page=1&size=5&sort=username,desc
    @GetMapping("/allUser")
    public ResponseEntity<Page<User>> userList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username,asc") String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Utils.parseSortParams(sort));
        Page<User> result = userService.findAllUser(pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@Validated @RequestBody User user) {

        if (userService.checkUserExists(user)) {
            throw new UserExistException("This user (username/email) is existing");
        }

        if (!Utils.isValidEmail(user.getEmail())) {
            throw new InputValidationException("Email is not valid.");
        }

        userService.registerUser(user);
        log.info("User registered successfully");
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/lend")
    public ResponseEntity<String> lend(@RequestBody Borrowing borrowing) {

//        User user = new User("u10", "$2a$12$hL.BBQQh3qFsVwdP.4VxfeLFZHf1PDxGtC9orafSZUIOaoi4UIoKm", "USER", "u10@gmail.com");
//        borrowing.setUser(user);
        borrowingService.lend(borrowing);
        log.info("borrowing is recored successfully");
        return ResponseEntity.ok("borrowing is recored successfully");
    }

    @GetMapping("/due")
    public ResponseEntity<List<BorrowingDTO>> getUsersWithDueDatesTodayAndTomorrow() {
        List<BorrowingDTO> borrowingDTOs = borrowingService.getUsersWithDueDatesTodayAndTomorrow();
        return new ResponseEntity<>(borrowingDTOs, HttpStatus.OK);
    }

    @GetMapping("/user/borrow")
    public ResponseEntity<List<UserBorrowingDTO>> getAllUsersByBorrowing() {
        List<UserBorrowingDTO> userBorrowingDTOs = borrowingService.getAllUsersByBorrowing();
        return new ResponseEntity<>(userBorrowingDTOs, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (userService.checkUserBorrowing(userId)) {
            throw new UserException("User is still borrowing book.");
        }
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/export-due")
    public ResponseEntity<String> exportBorrowingsToExcel(
            @RequestBody List<BorrowingDTO> borrowingDTOs) {
        borrowingService.exportBorrowingsToExcel(borrowingDTOs);
        return ResponseEntity.status(HttpStatus.OK).body("Borrowings exported to Excel successfully.");
    }

}
