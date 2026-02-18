package com.app.payloads;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDiscountDTO {

    private Long discountId;
    private String discountName;
    private Double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;

}
