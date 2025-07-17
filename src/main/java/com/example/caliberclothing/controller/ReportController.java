package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.CustomerDTO;
import com.example.caliberclothing.dto.OrderDTO;
import com.example.caliberclothing.entity.Inventory;
import com.example.caliberclothing.entity.Product;
import com.example.caliberclothing.entity.SupplierDetails;
import com.example.caliberclothing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('CEO')")
public class ReportController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getSalesReport(
            @RequestParam String period,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {

        try {
            LocalDateTime[] dateRange = calculateDateRange(period, startDate, endDate);
            LocalDateTime start = dateRange[0];
            LocalDateTime end = dateRange[1];

            List<OrderDTO> orders = orderService.getOrdersByDateRange(start, end);

            // Calculate sales metrics
            BigDecimal totalSales = orders.stream()
                    .map(OrderDTO::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int totalOrders = orders.size();

            BigDecimal averageOrderValue = totalOrders > 0 ?
                    totalSales.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP) :
                    BigDecimal.ZERO;

            // Group sales by date for trend analysis
            Map<String, BigDecimal> salesByDate = orders.stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getOrderDate().toLocalDate().toString(),
                            Collectors.mapping(OrderDTO::getTotalPrice,
                                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                    ));

            // Calculate growth compared to previous period
            LocalDateTime prevStart = start.minus(ChronoUnit.DAYS.between(start, end), ChronoUnit.DAYS);
            List<OrderDTO> prevOrders = orderService.getOrdersByDateRange(prevStart, start);
            BigDecimal prevSales = prevOrders.stream()
                    .map(OrderDTO::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal growthRate = prevSales.compareTo(BigDecimal.ZERO) > 0 ?
                    totalSales.subtract(prevSales).divide(prevSales, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)) :
                    BigDecimal.ZERO;

            Map<String, Object> report = new HashMap<>();
            report.put("period", period);
            report.put("startDate", start);
            report.put("endDate", end);
            report.put("totalSales", totalSales);
            report.put("totalOrders", totalOrders);
            report.put("averageOrderValue", averageOrderValue);
            report.put("salesByDate", salesByDate);
            report.put("growthRate", growthRate);
            report.put("previousPeriodSales", prevSales);

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to generate sales report: " + e.getMessage()));
        }
    }

    @GetMapping("/customer-growth")
    public ResponseEntity<Map<String, Object>> getCustomerGrowthReport(@RequestParam String period) {
        try {
            LocalDateTime[] dateRange = calculateDateRange(period, null, null);
            LocalDateTime start = dateRange[0];
            LocalDateTime end = dateRange[1];

            // Get customers created in the period
            List<CustomerDTO> newCustomers = customerService.getCustomersCreatedBetween(start, end);

            // Get total customers
            long totalCustomers = customerService.getTotalCustomersCount();

            // Calculate growth rate
            long newCustomerCount = newCustomers.size();
            long existingCustomers = totalCustomers - newCustomerCount;
            double growthRate = existingCustomers > 0 ? ((double) newCustomerCount / existingCustomers) * 100 : 0;

            // Group by registration date
            Map<String, Long> customersByDate = newCustomers.stream()
                    .collect(Collectors.groupingBy(
                            customer -> customer.getCreatedTimestamp().toLocalDate().toString(),
                            Collectors.counting()
                    ));

            // Calculate retention metrics (simplified)
            List<CustomerDTO> activeCustomers = customerService.getActiveCustomers();
            long activeCustomerCount = activeCustomers.size();
            double retentionRate = totalCustomers > 0 ? ((double) activeCustomerCount / totalCustomers) * 100 : 0;

            Map<String, Object> report = new HashMap<>();
            report.put("period", period);
            report.put("startDate", start);
            report.put("endDate", end);
            report.put("newCustomers", newCustomerCount);
            report.put("totalCustomers", totalCustomers);
            report.put("growthRate", growthRate);
            report.put("customersByDate", customersByDate);
            report.put("activeCustomers", activeCustomerCount);
            report.put("retentionRate", retentionRate);

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to generate customer growth report: " + e.getMessage()));
        }
    }

    @GetMapping("/most-sold-products")
    public ResponseEntity<Map<String, Object>> getMostSoldProductsReport(
            @RequestParam String period,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            LocalDateTime[] dateRange = calculateDateRange(period, null, null);
            LocalDateTime start = dateRange[0];
            LocalDateTime end = dateRange[1];

            List<OrderDTO> orders = orderService.getOrdersByDateRange(start, end);

            // For this implementation, we'll assume each order contains one product
            // In a real scenario, you'd have OrderItems with product details
            Map<String, Integer> productSales = new HashMap<>();
            Map<String, BigDecimal> productRevenue = new HashMap<>();

            // Since we don't have OrderItems, we'll create a simplified version
            // You should modify this based on your actual order structure
            for (OrderDTO order : orders) {
                String productKey = "Product_" + order.getId(); // Placeholder
                productSales.merge(productKey, order.getQuantity(), Integer::sum);
                productRevenue.merge(productKey, order.getTotalPrice(), BigDecimal::add);
            }

            // Sort by quantity sold
            List<Map.Entry<String, Integer>> sortedByQuantity = productSales.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(limit)
                    .collect(Collectors.toList());

            // Sort by revenue
            List<Map.Entry<String, BigDecimal>> sortedByRevenue = productRevenue.entrySet().stream()
                    .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                    .limit(limit)
                    .collect(Collectors.toList());

            Map<String, Object> report = new HashMap<>();
            report.put("period", period);
            report.put("startDate", start);
            report.put("endDate", end);
            report.put("topProductsByQuantity", sortedByQuantity);
            report.put("topProductsByRevenue", sortedByRevenue);
            report.put("totalProductsSold", productSales.values().stream().mapToInt(Integer::intValue).sum());

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to generate most sold products report: " + e.getMessage()));
        }
    }

    @GetMapping("/revenue-analysis")
    public ResponseEntity<Map<String, Object>> getRevenueAnalysisReport(
            @RequestParam String period,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        try {
            LocalDateTime[] dateRange = calculateDateRange(period, startDate, endDate);
            LocalDateTime start = dateRange[0];
            LocalDateTime end = dateRange[1];

            List<OrderDTO> orders = orderService.getOrdersByDateRange(start, end);

            BigDecimal totalRevenue = orders.stream()
                    .map(OrderDTO::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Calculate monthly revenue breakdown
            Map<String, BigDecimal> monthlyRevenue = orders.stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getOrderDate().getYear() + "-" +
                                    String.format("%02d", order.getOrderDate().getMonthValue()),
                            Collectors.mapping(OrderDTO::getTotalPrice,
                                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                    ));

            // Calculate average order value by month
            Map<String, Double> avgOrderValueByMonth = orders.stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getOrderDate().getYear() + "-" +
                                    String.format("%02d", order.getOrderDate().getMonthValue()),
                            Collectors.averagingDouble(order -> order.getTotalPrice().doubleValue())
                    ));

            // Revenue trend analysis
            List<String> months = monthlyRevenue.keySet().stream().sorted().collect(Collectors.toList());
            List<BigDecimal> revenueValues = months.stream()
                    .map(monthlyRevenue::get)
                    .collect(Collectors.toList());

            Map<String, Object> report = new HashMap<>();
            report.put("period", period);
            report.put("startDate", start);
            report.put("endDate", end);
            report.put("totalRevenue", totalRevenue);
            report.put("monthlyRevenue", monthlyRevenue);
            report.put("avgOrderValueByMonth", avgOrderValueByMonth);
            report.put("revenuetrend", Map.of("months", months, "values", revenueValues));
            report.put("totalOrders", orders.size());

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to generate revenue analysis report: " + e.getMessage()));
        }
    }

    @GetMapping("/inventory-status")
    public ResponseEntity<Map<String, Object>> getInventoryStatusReport() {
        try {
            List<Inventory> allInventory = inventoryService.getAllInventories();
            List<Inventory> lowStockItems = inventoryService.getLowStockItems();
            List<Product> allProducts = productService.getAllActiveProducts();

            // Calculate inventory metrics
            int totalProducts = allProducts.size();
            int lowStockCount = lowStockItems.size();
            int adequateStockCount = totalProducts - lowStockCount;

            // Calculate total inventory value
            BigDecimal totalInventoryValue = allProducts.stream()
                    .map(product -> product.getCostPrice().multiply(BigDecimal.valueOf(product.getQuantityInStock())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Group products by stock level
            Map<String, Integer> stockLevels = new HashMap<>();
            stockLevels.put("Low Stock", lowStockCount);
            stockLevels.put("Adequate Stock", adequateStockCount);

            // Calculate turnover ratio (simplified)
            // This would need sales data to be accurate
            double avgTurnoverRatio = 2.5; // Placeholder

            // Top low stock items
            List<Map<String, Object>> topLowStockItems = lowStockItems.stream()
                    .limit(10)
                    .map(inventory -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("productId", inventory.getProduct().getId());
                        item.put("productName", inventory.getProduct().getName());
                        item.put("currentStock", inventory.getTotalQuantityPurchasing());
                        item.put("reorderLevel", inventory.getReorderLevel());
                        return item;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> report = new HashMap<>();
            report.put("totalProducts", totalProducts);
            report.put("lowStockCount", lowStockCount);
            report.put("adequateStockCount", adequateStockCount);
            report.put("totalInventoryValue", totalInventoryValue);
            report.put("stockLevels", stockLevels);
            report.put("avgTurnoverRatio", avgTurnoverRatio);
            report.put("topLowStockItems", topLowStockItems);
            report.put("generatedAt", LocalDateTime.now());

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to generate inventory status report: " + e.getMessage()));
        }
    }

    @GetMapping("/order-status-distribution")
    public ResponseEntity<Map<String, Object>> getOrderStatusDistributionReport(@RequestParam String period) {
        try {
            LocalDateTime[] dateRange = calculateDateRange(period, null, null);
            LocalDateTime start = dateRange[0];
            LocalDateTime end = dateRange[1];

            List<OrderDTO> orders = orderService.getOrdersByDateRange(start, end);

            // Group orders by status
            Map<String, Long> statusDistribution = orders.stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getOrderStatus() != null ? order.getOrderStatus().getValue() : "Unknown",
                            Collectors.counting()
                    ));

            // Calculate percentages
            long totalOrders = orders.size();
            Map<String, Double> statusPercentages = statusDistribution.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> totalOrders > 0 ? (entry.getValue().doubleValue() / totalOrders) * 100 : 0
                    ));

            // Calculate completion rate
            long completedOrders = statusDistribution.getOrDefault("Completed", 0L);
            double completionRate = totalOrders > 0 ? ((double) completedOrders / totalOrders) * 100 : 0;

            Map<String, Object> report = new HashMap<>();
            report.put("period", period);
            report.put("startDate", start);
            report.put("endDate", end);
            report.put("totalOrders", totalOrders);
            report.put("statusDistribution", statusDistribution);
            report.put("statusPercentages", statusPercentages);
            report.put("completionRate", completionRate);

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to generate order status distribution report: " + e.getMessage()));
        }
    }

    @GetMapping("/supplier-performance")
    public ResponseEntity<Map<String, Object>> getSupplierPerformanceReport(@RequestParam String period) {
        try {
            List<SupplierDetails> suppliers = supplierDetailsService.getAllActiveSuppliers();

            // For this simplified version, we'll create mock performance data
            // In a real implementation, you'd track delivery times, quality scores, etc.
            List<Map<String, Object>> supplierPerformance = suppliers.stream()
                    .map(supplier -> {
                        Map<String, Object> performance = new HashMap<>();
                        performance.put("supplierId", supplier.getId());
                        performance.put("supplierName", supplier.getSupplierName());
                        performance.put("country", supplier.getCountry());
                        // Mock data - replace with actual calculations
                        performance.put("onTimeDeliveryRate", 85.5 + (Math.random() * 14.5)); // 85.5-100%
                        performance.put("qualityScore", 4.0 + Math.random()); // 4.0-5.0
                        performance.put("totalOrders", (int)(Math.random() * 50) + 10); // 10-60 orders
                        performance.put("avgDeliveryDays", 5 + (int)(Math.random() * 10)); // 5-15 days
                        return performance;
                    })
                    .collect(Collectors.toList());

            // Sort by performance score (combination of delivery rate and quality)
            supplierPerformance.sort((a, b) -> {
                double scoreA = ((Double) a.get("onTimeDeliveryRate")) * 0.6 + ((Double) a.get("qualityScore")) * 20 * 0.4;
                double scoreB = ((Double) b.get("onTimeDeliveryRate")) * 0.6 + ((Double) b.get("qualityScore")) * 20 * 0.4;
                return Double.compare(scoreB, scoreA);
            });

            Map<String, Object> report = new HashMap<>();
            report.put("period", period);
            report.put("totalSuppliers", suppliers.size());
            report.put("supplierPerformance", supplierPerformance);
            report.put("topPerformers", supplierPerformance.stream().limit(5).collect(Collectors.toList()));
            report.put("generatedAt", LocalDateTime.now());

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to generate supplier performance report: " + e.getMessage()));
        }
    }

    @GetMapping("/customer-demographics")
    public ResponseEntity<Map<String, Object>> getCustomerDemographicsReport() {
        try {
            List<CustomerDTO> customers = customerService.getAllCustomers();

            // Age distribution (simplified - you'd need birth dates)
            Map<String, Long> ageGroups = Map.of(
                    "18-25", (long)(customers.size() * 0.25),
                    "26-35", (long)(customers.size() * 0.35),
                    "36-45", (long)(customers.size() * 0.25),
                    "46-60", (long)(customers.size() * 0.15)
            );

            // Gender distribution
            Map<String, Long> genderDistribution = customers.stream()
                    .collect(Collectors.groupingBy(
                            customer -> customer.getSex() != null ? customer.getSex() : "Unknown",
                            Collectors.counting()
                    ));

            // Geographic distribution
            Map<String, Long> countryDistribution = customers.stream()
                    .collect(Collectors.groupingBy(
                            customer -> customer.getCountry() != null ? customer.getCountry() : "Unknown",
                            Collectors.counting()
                    ));

            // Registration trends (last 12 months)
            LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
            List<CustomerDTO> recentCustomers = customerService.getCustomersCreatedBetween(twelveMonthsAgo, LocalDateTime.now());

            Map<String, Long> registrationByMonth = recentCustomers.stream()
                    .collect(Collectors.groupingBy(
                            customer -> customer.getCreatedTimestamp().getYear() + "-" +
                                    String.format("%02d", customer.getCreatedTimestamp().getMonthValue()),
                            Collectors.counting()
                    ));

            Map<String, Object> report = new HashMap<>();
            report.put("totalCustomers", customers.size());
            report.put("ageGroups", ageGroups);
            report.put("genderDistribution", genderDistribution);
            report.put("countryDistribution", countryDistribution);
            report.put("registrationByMonth", registrationByMonth);
            report.put("newCustomersLast12Months", recentCustomers.size());
            report.put("generatedAt", LocalDateTime.now());

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to generate customer demographics report: " + e.getMessage()));
        }
    }

    // Utility method to calculate date ranges based on period
    private LocalDateTime[] calculateDateRange(String period, LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime end = endDate != null ? endDate : LocalDateTime.now();
        LocalDateTime start;

        if (startDate != null) {
            start = startDate;
        } else {
            switch (period.toLowerCase()) {
                case "week":
                    start = end.minusWeeks(1);
                    break;
                case "month":
                    start = end.minusMonths(1);
                    break;
                case "3months":
                    start = end.minusMonths(3);
                    break;
                case "6months":
                    start = end.minusMonths(6);
                    break;
                case "year":
                    start = end.minusYears(1);
                    break;
                case "2years":
                    start = end.minusYears(2);
                    break;
                case "3years":
                    start = end.minusYears(3);
                    break;
                default:
                    start = end.minusMonths(1); // Default to 1 month
            }
        }

        return new LocalDateTime[]{start, end};
    }
}