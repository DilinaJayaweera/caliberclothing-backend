package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.GrnStatusDTO;
import com.example.caliberclothing.entity.GrnStatus;

import java.util.List;
import java.util.Optional;

public interface GrnStatusService {

    public List<GrnStatus> getAllGrnStatuses();

    public Optional<GrnStatus> getGrnStatusById(Integer id);

    public GrnStatus createGrnStatus(GrnStatusDTO grnStatusDTO);

    public GrnStatus updateGrnStatus(Integer id, GrnStatusDTO grnStatusDTO);

    public void deleteGrnStatus(Integer id);

    public Optional<GrnStatus> getGrnStatusByValue(String value);
}
