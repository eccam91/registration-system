package com.engeto.registrationsystem.repository;

import com.engeto.registrationsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameAndSurnameAndPersonID(String name, String surname, String personID);
}