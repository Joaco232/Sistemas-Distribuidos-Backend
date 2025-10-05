package com.movienow.backend.repositories;

import com.movienow.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findUsersById(Long id);

    public Optional<User> findUserByEmail(String email);

    public Boolean existsByEmail(String email);





}
