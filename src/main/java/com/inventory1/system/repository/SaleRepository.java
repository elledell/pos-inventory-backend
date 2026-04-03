package com.inventory1.system.repository;

import com.inventory1.system.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    // This custom query asks MySQL to add up the totalAmount column for any sales between two dates
    // COALESCE ensures that if there are 0 sales, it returns 0.0 instead of a crash/null.
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.saleDate >= :startDate AND s.saleDate <= :endDate")
    Double sumSalesBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // ==========================================
    // 🔥 NEW METHOD: Fetch the actual receipts 🔥
    // ==========================================
    // This tells Spring Boot to find all full Sale receipts that happened between two specific times
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}