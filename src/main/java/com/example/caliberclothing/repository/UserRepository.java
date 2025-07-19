package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    Optional<User> findByUsernameAndIsActiveTrue(String username);

    @Query("SELECT DISTINCT u.role FROM User u WHERE u.role IS NOT NULL")
    List<String> findAllDistinctRoles();


}
