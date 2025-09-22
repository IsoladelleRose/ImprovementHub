package com.innostore.improvementhub.repository;

import com.innostore.improvementhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String emailAddress);

    boolean existsByEmail(String emailAddress);

    Optional<User> findByEmailAndPassword(String emailAddress, String password);
}