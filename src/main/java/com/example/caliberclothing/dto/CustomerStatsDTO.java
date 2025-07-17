package com.example.caliberclothing.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStatsDTO {
    private long totalCustomers;
    private long newCustomersThisMonth;
    private long activeCustomers;
    private long inactiveCustomers;
    private String topProvince;
    private String mostCommonAge;
}
