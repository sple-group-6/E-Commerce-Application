package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.ReviewDTO;
import com.app.payloads.ReviewRequestDTO;
import com.app.services.ReviewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/public/users/{email}/products/{productId}/reviews")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable String email,
            @PathVariable Long productId,
            @Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewDTO reviewDTO = reviewService.addReview(email, productId, reviewRequestDTO);
        return new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products/{productId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getProductReviews(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PutMapping("/public/users/{email}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable String email,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewDTO reviewDTO = reviewService.updateReview(email, reviewId, reviewRequestDTO);
        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @DeleteMapping("/public/users/{email}/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable String email,
            @PathVariable Long reviewId) {
        String message = reviewService.deleteReview(email, reviewId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
