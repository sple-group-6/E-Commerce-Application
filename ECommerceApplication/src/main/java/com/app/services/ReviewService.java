package com.app.services;

import java.util.List;

import com.app.payloads.ReviewDTO;
import com.app.payloads.ReviewRequestDTO;

public interface ReviewService {

    ReviewDTO addReview(String email, Long productId, ReviewRequestDTO reviewRequestDTO);

    List<ReviewDTO> getProductReviews(Long productId);

    ReviewDTO updateReview(String email, Long reviewId, ReviewRequestDTO reviewRequestDTO);

    String deleteReview(String email, Long reviewId);
}
