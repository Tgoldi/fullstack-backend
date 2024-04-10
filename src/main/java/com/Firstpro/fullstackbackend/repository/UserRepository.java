package com.Firstpro.fullstackbackend.repository;

import com.Firstpro.fullstackbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
