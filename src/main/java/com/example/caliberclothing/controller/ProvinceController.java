package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.Province;
import com.example.caliberclothing.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    @Autowired
    private ProvinceRepository provinceRepository;

    @GetMapping
    public ResponseEntity<List<Province>> getAllProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        return ResponseEntity.ok(provinces);
    }
}
