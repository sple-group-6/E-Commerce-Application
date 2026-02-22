package com.app.payloads;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long reviewId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private ProductDTO product;
    private Integer stars;
    private String reviewText;
    private LocalDate reviewDate;
}
