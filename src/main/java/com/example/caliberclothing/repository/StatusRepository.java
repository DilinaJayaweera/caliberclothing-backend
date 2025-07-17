package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    Optional<Status> findByValue(String value);
//    List<Status> findByIsActiveTrue();
}
