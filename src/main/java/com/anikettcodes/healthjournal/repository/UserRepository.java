package com.anikettcodes.healthjournal.repository;

import com.anikettcodes.healthjournal.domain.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @NonNull
    Optional<User> findById(@NonNull Long id);

    boolean existsByEmail(String email);

    @NonNull
    User save(@NonNull User user);



}
