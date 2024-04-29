package com.example.demo.repositories.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.User;
import com.example.demo.models.UserDetail;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByUser(User user);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
