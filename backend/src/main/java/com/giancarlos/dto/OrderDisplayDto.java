package com.giancarlos.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDisplayDto {
    private Long id;
    private String date;
    private BigDecimal total;
    private String address;
}
