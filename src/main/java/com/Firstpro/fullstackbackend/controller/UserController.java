package com.Firstpro.fullstackbackend.controller;

import com.Firstpro.fullstackbackend.exception.UserNotFoundException;
import com.Firstpro.fullstackbackend.model.User;
import com.Firstpro.fullstackbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    User newUser(@RequestBody User newUser){
        if (newUser.getUsername() == null || newUser.getUsername().trim().isEmpty()
                || newUser.getUsername().length() > 100) {
            throw new IllegalArgumentException("Username is required and must be at most 100 characters.");
        }
        if (newUser.getName() == null || newUser.getName().trim().isEmpty()
                || newUser.getName().length() > 100) {
            throw new IllegalArgumentException("Name is required and must be at most 100 characters.");
        }
        if (newUser.getEmail() == null || newUser.getEmail().trim().isEmpty()
                || !newUser.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("A valid email is required.");
        }
        return userRepository.save(newUser);
    }

    @GetMapping("/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
    }
    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser,@PathVariable Long id){
        if (newUser.getUsername() == null || newUser.getUsername().trim().isEmpty()
                || newUser.getUsername().length() > 100) {
            throw new IllegalArgumentException("Username is required and must be at most 100 characters.");
        }
        if (newUser.getName() == null || newUser.getName().trim().isEmpty()
                || newUser.getName().length() > 100) {
            throw new IllegalArgumentException("Name is required and must be at most 100 characters.");
        }
        if (newUser.getEmail() == null || newUser.getEmail().trim().isEmpty()
                || !newUser.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("A valid email is required.");
        }
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                }).orElseThrow(()->new UserNotFoundException(id));
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id){
        if (!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return "User with id "+id+" has been deleted Successfully.";
    }




}
