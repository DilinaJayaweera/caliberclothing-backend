package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.GrnDTO;
import com.example.caliberclothing.entity.Employee;
import com.example.caliberclothing.entity.Grn;
import com.example.caliberclothing.entity.GrnStatus;
import com.example.caliberclothing.entity.SupplierDetails;
import com.example.caliberclothing.repository.EmployeeRepository;
import com.example.caliberclothing.repository.GrnRepository;
import com.example.caliberclothing.repository.GrnStatusRepository;
import com.example.caliberclothing.repository.SupplierDetailsRepository;
import com.example.caliberclothing.service.GrnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GrnServiceImpl implements GrnService {

    @Autowired
    private GrnRepository grnRepository;

    @Autowired
    private GrnStatusRepository grnStatusRepository;

    public List<Grn> getAllGrns() {
        return grnRepository.findAll();
    }

    public Optional<Grn> getGrnById(Integer id) {
        return grnRepository.findById(id);
    }

    public Grn createGrn(GrnDTO grnDTO) {
        Grn grn = Grn.builder()
                .receivedDate(grnDTO.getReceivedDate())
                .employee(Employee.builder().id(grnDTO.getEmployee().getId()).build())
                .supplierDetails(SupplierDetails.builder().id(grnDTO.getSupplierDetails().getId()).build())
                .status(GrnStatus.builder().id(grnDTO.getStatus().getId()).build())
                .build();

        return grnRepository.save(grn);
    }

    public Grn updateGrn(Integer id, GrnDTO grnDTO) {
        Optional<Grn> existingGrn = grnRepository.findById(id);
        if (existingGrn.isPresent()) {
            Grn grn = existingGrn.get();
            grn.setReceivedDate(grnDTO.getReceivedDate());
            grn.setEmployee(Employee.builder().id(grnDTO.getEmployee().getId()).build());
            grn.setSupplierDetails(SupplierDetails.builder().id(grnDTO.getSupplierDetails().getId()).build());
            grn.setStatus(GrnStatus.builder().id(grnDTO.getStatus().getId()).build());

            return grnRepository.save(grn);
        }
        throw new RuntimeException("GRN not found with id: " + id);
    }

    public void deleteGrn(Integer id) {
        grnRepository.deleteById(id);
    }

    public List<Grn> getGrnsByDateRange(LocalDate startDate, LocalDate endDate) {
        return grnRepository.findByReceivedDateBetween(startDate, endDate);
    }

    public List<Grn> getGrnsByEmployee(Integer employeeId) {
        return grnRepository.findByEmployeeId(employeeId);
    }

    public List<Grn> getGrnsBySupplier(Integer supplierDetailsId) {
        return grnRepository.findBySupplierDetailsId(supplierDetailsId);
    }

    public List<Grn> getGrnsByStatus(Integer statusId) {
        return grnRepository.findByStatusId(statusId);
    }

    public List<Grn> getGrnsByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, Integer statusId) {
        return grnRepository.findByDateRangeAndStatus(startDate, endDate, statusId);
    }
}