package com.kodera.authservice.repository;

import com.kodera.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // should become select * from users where email==
    boolean existsByEmail(String email); // select count(*) from users where email==
}
