package com.inventory1.system.controller;

import com.inventory1.system.dto.DashboardStatsDTO;
import com.inventory1.system.entity.Sale;
import com.inventory1.system.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReportController {

    private final SaleRepository saleRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Calculate Daily (From midnight today to right now)
        LocalDateTime startOfDay = now.truncatedTo(ChronoUnit.DAYS);
        Double daily = saleRepository.sumSalesBetweenDates(startOfDay, now);

        // 2. Calculate Weekly (From Monday of this week to right now)
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).truncatedTo(ChronoUnit.DAYS);
        Double weekly = saleRepository.sumSalesBetweenDates(startOfWeek, now);

        // 3. Calculate Monthly (From the 1st of this month to right now)
        LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
        Double monthly = saleRepository.sumSalesBetweenDates(startOfMonth, now);

        // Package it up and send it to React
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setDailySales(daily);
        stats.setWeeklySales(weekly);
        stats.setMonthlySales(monthly);

        return ResponseEntity.ok(stats);
    }

    // ==========================================
    // 🔥 NEW ENDPOINT: Get Today's Detailed Receipts 🔥
    // ==========================================
    @GetMapping("/sales/today")
    public ResponseEntity<List<Sale>> getTodaySalesDetails() {

        // Find exactly when today started (00:00:00)
        LocalDateTime startOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        // Find exactly when today ends (23:59:59.999999999)
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        // Fetch all full receipts stamped with today's date from the filing cabinet
        List<Sale> todaySales = saleRepository.findBySaleDateBetween(startOfDay, endOfDay);

        // Hand them over to React!
        return ResponseEntity.ok(todaySales);
    }
    // ==========================================
    // 🔥 GET THIS WEEK'S Detailed Receipts 🔥
    // ==========================================
    @GetMapping("/sales/weekly")
    public ResponseEntity<List<Sale>> getWeeklySalesDetails() {
        LocalDateTime startOfWeek = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);

        List<Sale> weeklySales = saleRepository.findBySaleDateBetween(startOfWeek, endOfDay);
        return ResponseEntity.ok(weeklySales);
    }

    // ==========================================
    // 🔥 GET THIS MONTH'S Detailed Receipts 🔥
    // ==========================================
    @GetMapping("/sales/monthly")
    public ResponseEntity<List<Sale>> getMonthlySalesDetails() {
        LocalDateTime startOfMonth = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);

        List<Sale> monthlySales = saleRepository.findBySaleDateBetween(startOfMonth, endOfDay);
        return ResponseEntity.ok(monthlySales);
    }
}