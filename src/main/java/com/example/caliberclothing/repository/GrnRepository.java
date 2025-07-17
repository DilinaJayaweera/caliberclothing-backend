package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Grn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrnRepository extends JpaRepository<Grn, Integer> {

    List<Grn> findByReceivedDateBetween(LocalDate startDate, LocalDate endDate);

    List<Grn> findByEmployeeId(Integer employeeId);

    List<Grn> findBySupplierDetailsId(Integer supplierDetailsId);

    List<Grn> findByStatusId(Integer statusId);

    @Query("SELECT g FROM Grn g WHERE g.receivedDate >= :startDate AND g.receivedDate <= :endDate AND g.status.id = :statusId")
    List<Grn> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("statusId") Integer statusId);
}
