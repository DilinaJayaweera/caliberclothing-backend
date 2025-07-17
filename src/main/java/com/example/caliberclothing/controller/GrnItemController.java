package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.GrnItemDTO;
import com.example.caliberclothing.entity.GrnItem;
import com.example.caliberclothing.service.GrnItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grn-items")
//@CrossOrigin(origins = "*")
public class GrnItemController {

    @Autowired
    private GrnItemService grnItemService;

    @GetMapping
    public ResponseEntity<List<GrnItem>> getAllGrnItems() {
        List<GrnItem> grnItems = grnItemService.getAllGrnItems();
        return ResponseEntity.ok(grnItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrnItem> getGrnItemById(@PathVariable Integer id) {
        Optional<GrnItem> grnItem = grnItemService.getGrnItemById(id);
        return grnItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GrnItem> createGrnItem(@Valid @RequestBody GrnItemDTO grnItemDTO) {
        GrnItem createdGrnItem = grnItemService.createGrnItem(grnItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrnItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrnItem> updateGrnItem(@PathVariable Integer id, @Valid @RequestBody GrnItemDTO grnItemDTO) {
        try {
            GrnItem updatedGrnItem = grnItemService.updateGrnItem(id, grnItemDTO);
            return ResponseEntity.ok(updatedGrnItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrnItem(@PathVariable Integer id) {
        grnItemService.deleteGrnItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/grn/{grnId}")
    public ResponseEntity<List<GrnItem>> getGrnItemsByGrnId(@PathVariable Integer grnId) {
        List<GrnItem> grnItems = grnItemService.getGrnItemsByGrnId(grnId);
        return ResponseEntity.ok(grnItems);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<GrnItem>> getGrnItemsByProductId(@PathVariable Integer productId) {
        List<GrnItem> grnItems = grnItemService.getGrnItemsByProductId(productId);
        return ResponseEntity.ok(grnItems);
    }
}
