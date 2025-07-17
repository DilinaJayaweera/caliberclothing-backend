package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.GrnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrnStatusRepository extends JpaRepository<GrnStatus, Integer> {

    Optional<GrnStatus> findByValue(String value);
}
