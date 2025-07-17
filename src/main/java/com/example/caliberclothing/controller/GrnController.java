package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.GrnDTO;
import com.example.caliberclothing.entity.Grn;
import com.example.caliberclothing.service.GrnService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grn")
//@CrossOrigin(origins = "*")
public class GrnController {

    @Autowired
    private GrnService grnService;

    @GetMapping
    public ResponseEntity<List<Grn>> getAllGrns() {
        List<Grn> grns = grnService.getAllGrns();
        return ResponseEntity.ok(grns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grn> getGrnById(@PathVariable Integer id) {
        Optional<Grn> grn = grnService.getGrnById(id);
        return grn.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Grn> createGrn(@Valid @RequestBody GrnDTO grnDTO) {
        Grn createdGrn = grnService.createGrn(grnDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrn);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grn> updateGrn(@PathVariable Integer id, @Valid @RequestBody GrnDTO grnDTO) {
        try {
            Grn updatedGrn = grnService.updateGrn(id, grnDTO);
            return ResponseEntity.ok(updatedGrn);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrn(@PathVariable Integer id) {
        grnService.deleteGrn(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Grn>> getGrnsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<Grn> grns = grnService.getGrnsByDateRange(startDate, endDate);
        return ResponseEntity.ok(grns);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Grn>> getGrnsByEmployee(@PathVariable Integer employeeId) {
        List<Grn> grns = grnService.getGrnsByEmployee(employeeId);
        return ResponseEntity.ok(grns);
    }

    @GetMapping("/supplier/{supplierDetailsId}")
    public ResponseEntity<List<Grn>> getGrnsBySupplier(@PathVariable Integer supplierDetailsId) {
        List<Grn> grns = grnService.getGrnsBySupplier(supplierDetailsId);
        return ResponseEntity.ok(grns);
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<Grn>> getGrnsByStatus(@PathVariable Integer statusId) {
        List<Grn> grns = grnService.getGrnsByStatus(statusId);
        return ResponseEntity.ok(grns);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Grn>> getGrnsByDateRangeAndStatus(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam Integer statusId) {
        List<Grn> grns = grnService.getGrnsByDateRangeAndStatus(startDate, endDate, statusId);
        return ResponseEntity.ok(grns);
    }
}
