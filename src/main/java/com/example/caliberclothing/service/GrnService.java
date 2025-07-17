package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.GrnDTO;
import com.example.caliberclothing.entity.Employee;
import com.example.caliberclothing.entity.Grn;
import com.example.caliberclothing.entity.GrnStatus;
import com.example.caliberclothing.entity.SupplierDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GrnService {
    public List<Grn> getAllGrns();

    public Optional<Grn> getGrnById(Integer id);

    public Grn createGrn(GrnDTO grnDTO);

    public Grn updateGrn(Integer id, GrnDTO grnDTO);

    public void deleteGrn(Integer id);

    public List<Grn> getGrnsByDateRange(LocalDate startDate, LocalDate endDate);

    public List<Grn> getGrnsByEmployee(Integer employeeId);

    public List<Grn> getGrnsBySupplier(Integer supplierDetailsId);

    public List<Grn> getGrnsByStatus(Integer statusId);

    public List<Grn> getGrnsByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, Integer statusId);
}
