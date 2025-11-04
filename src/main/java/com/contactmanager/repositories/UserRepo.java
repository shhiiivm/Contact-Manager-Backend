package com.contactmanager.repositories;

import com.contactmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, String>
{

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String passwprd);

    Optional<User> findByEmailToken(String id);





}
