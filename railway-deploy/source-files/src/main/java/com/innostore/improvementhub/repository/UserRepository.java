package com.innostore.improvementhub.repository;

import com.innostore.improvementhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAddress(String emailAddress);

    boolean existsByEmailAddress(String emailAddress);

    Optional<User> findByEmailAddressAndPassword(String emailAddress, String password);
}