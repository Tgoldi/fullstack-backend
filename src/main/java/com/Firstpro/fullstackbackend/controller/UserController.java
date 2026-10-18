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
        User toCreate = new User();
        toCreate.setId(null);
        toCreate.setUsername(newUser.getUsername());
        toCreate.setName(newUser.getName());
        toCreate.setEmail(newUser.getEmail());
        return userRepository.save(toCreate);
    }

    @GetMapping("/users")
    List<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "20") int size){
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        return userRepository.findAll(org.springframework.data.domain.PageRequest.of(safePage, safeSize)).getContent();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
    }
    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser,@PathVariable Long id){
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
