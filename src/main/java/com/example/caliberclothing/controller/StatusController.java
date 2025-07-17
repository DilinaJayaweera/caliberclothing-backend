package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.Status;
import com.example.caliberclothing.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class StatusController {

    @Autowired
    private StatusRepository statusRepository;

    @GetMapping
    public ResponseEntity<List<Status>> getAllStatuses() {
        List<Status> statuses = statusRepository.findAll();
        return ResponseEntity.ok(statuses);
    }
}
