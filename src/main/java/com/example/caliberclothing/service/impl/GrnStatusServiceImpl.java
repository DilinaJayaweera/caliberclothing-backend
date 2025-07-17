package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.GrnStatusDTO;
import com.example.caliberclothing.entity.GrnStatus;
import com.example.caliberclothing.repository.GrnStatusRepository;
import com.example.caliberclothing.service.GrnStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GrnStatusServiceImpl implements GrnStatusService {

    @Autowired
    private GrnStatusRepository grnStatusRepository;

    public List<GrnStatus> getAllGrnStatuses() {
        return grnStatusRepository.findAll();
    }

    public Optional<GrnStatus> getGrnStatusById(Integer id) {
        return grnStatusRepository.findById(id);
    }

    public GrnStatus createGrnStatus(GrnStatusDTO grnStatusDTO) {
        GrnStatus grnStatus = GrnStatus.builder()
                .value(grnStatusDTO.getValue())
                .build();

        return grnStatusRepository.save(grnStatus);
    }

    public GrnStatus updateGrnStatus(Integer id, GrnStatusDTO grnStatusDTO) {
        Optional<GrnStatus> existingGrnStatus = grnStatusRepository.findById(id);
        if (existingGrnStatus.isPresent()) {
            GrnStatus grnStatus = existingGrnStatus.get();
            grnStatus.setValue(grnStatusDTO.getValue());

            return grnStatusRepository.save(grnStatus);
        }
        throw new RuntimeException("GRN Status not found with id: " + id);
    }

    public void deleteGrnStatus(Integer id) {
        grnStatusRepository.deleteById(id);
    }

    public Optional<GrnStatus> getGrnStatusByValue(String value) {
        return grnStatusRepository.findByValue(value);
    }
}
