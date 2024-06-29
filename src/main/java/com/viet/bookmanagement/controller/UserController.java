package com.viet.bookmanagement.controller;

import com.viet.bookmanagement.entities.User;
import com.viet.bookmanagement.exception.UserNotFoundException;
import com.viet.bookmanagement.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity<User> profile(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("User is not existing.");
        }

        return ResponseEntity.ok(user);
    }




}
