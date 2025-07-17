package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.GrnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GrnItemRepository extends JpaRepository<GrnItem, Integer> {

    List<GrnItem> findByGrnId(Integer grnId);

    List<GrnItem> findByProductId(Integer productId);

    @Query("SELECT gi FROM GrnItem gi WHERE gi.grn.id = :grnId")
    List<GrnItem> findItemsByGrnId(@Param("grnId") Integer grnId);
}