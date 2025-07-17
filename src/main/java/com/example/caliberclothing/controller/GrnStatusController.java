package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.GrnStatusDTO;
import com.example.caliberclothing.entity.GrnStatus;
import com.example.caliberclothing.service.GrnStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/grn-status")
//@CrossOrigin(origins = "*")
public class GrnStatusController {

    @Autowired
    private GrnStatusService grnStatusService;

    @GetMapping
    public ResponseEntity<List<GrnStatus>> getAllGrnStatuses() {
        List<GrnStatus> grnStatuses = grnStatusService.getAllGrnStatuses();
        return ResponseEntity.ok(grnStatuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrnStatus> getGrnStatusById(@PathVariable Integer id) {
        Optional<GrnStatus> grnStatus = grnStatusService.getGrnStatusById(id);
        return grnStatus.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GrnStatus> createGrnStatus(@Valid @RequestBody GrnStatusDTO grnStatusDTO) {
        GrnStatus createdGrnStatus = grnStatusService.createGrnStatus(grnStatusDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrnStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrnStatus> updateGrnStatus(@PathVariable Integer id, @Valid @RequestBody GrnStatusDTO grnStatusDTO) {
        try {
            GrnStatus updatedGrnStatus = grnStatusService.updateGrnStatus(id, grnStatusDTO);
            return ResponseEntity.ok(updatedGrnStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrnStatus(@PathVariable Integer id) {
        grnStatusService.deleteGrnStatus(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/value/{value}")
    public ResponseEntity<GrnStatus> getGrnStatusByValue(@PathVariable String value) {
        Optional<GrnStatus> grnStatus = grnStatusService.getGrnStatusByValue(value);
        return grnStatus.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
