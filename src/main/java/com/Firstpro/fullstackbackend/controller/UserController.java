package com.Firstpro.fullstackbackend.controller;

import com.Firstpro.fullstackbackend.exception.UserNotFoundException;
import com.Firstpro.fullstackbackend.model.User;
import com.Firstpro.fullstackbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private boolean isSelfOrAdmin(Authentication authentication, Long id) {
        if (authentication == null) {
            return false;
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }
        return authentication.getName() != null
                && userRepository.findById(id)
                        .map(u -> u.getUsername() != null && u.getUsername().equals(authentication.getName()))
                        .orElse(false);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @PostMapping("/user")
    User newUser(@Valid @RequestBody User newUser){
        return userRepository.save(newUser);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id, Authentication authentication){
        if (!isSelfOrAdmin(authentication, id)) {
            throw new UserNotFoundException(id);
        }
        return userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/user/{id}")
    User updateUser(@Valid @RequestBody User newUser,@PathVariable Long id, Authentication authentication){
        if (!isSelfOrAdmin(authentication, id)) {
            throw new UserNotFoundException(id);
        }
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                }).orElseThrow(()->new UserNotFoundException(id));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id, Authentication authentication){
        if (!isSelfOrAdmin(authentication, id)) {
            throw new UserNotFoundException(id);
        }
        if (!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return "User with id "+id+" has been deleted Successfully.";
    }




}
