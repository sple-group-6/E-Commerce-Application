package com.app.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.Review;
import com.app.entites.User;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.ProductDTO;
import com.app.payloads.ReviewDTO;
import com.app.payloads.ReviewRequestDTO;
import com.app.repositories.ProductRepo;
import com.app.repositories.ReviewRepo;
import com.app.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ReviewDTO addReview(String email, Long productId, ReviewRequestDTO reviewRequestDTO) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (!reviewRepo.hasUserPurchasedProduct(email, productId)) {
            throw new APIException("You can only review products you have purchased");
        }

        Review existingReview = reviewRepo.findByUserEmailAndProductProductId(email, productId);
        if (existingReview != null) {
            throw new APIException("You have already reviewed this product");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setStars(reviewRequestDTO.getStars());
        review.setReviewText(reviewRequestDTO.getReviewText());
        review.setReviewDate(LocalDate.now());

        Review savedReview = reviewRepo.save(review);

        return mapToReviewDTO(savedReview);
    }

    @Override
    public List<ReviewDTO> getProductReviews(Long productId) {
        productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        return reviewRepo.findByProductProductId(productId).stream()
                .map(this::mapToReviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO updateReview(String email, Long reviewId, ReviewRequestDTO reviewRequestDTO) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", reviewId));

        if (!review.getUser().getEmail().equals(email)) {
            throw new APIException("You are not authorized to update this review");
        }

        review.setStars(reviewRequestDTO.getStars());
        review.setReviewText(reviewRequestDTO.getReviewText());

        Review updatedReview = reviewRepo.save(review);

        return mapToReviewDTO(updatedReview);
    }

    @Override
    public String deleteReview(String email, Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", reviewId));

        if (!review.getUser().getEmail().equals(email)) {
            throw new APIException("You are not authorized to delete this review");
        }

        reviewRepo.delete(review);

        return "Review for product '" + review.getProduct().getProductName() + "' deleted successfully";
    }

    private ReviewDTO mapToReviewDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setUserEmail(review.getUser().getEmail());
        dto.setUserFirstName(review.getUser().getFirstName());
        dto.setUserLastName(review.getUser().getLastName());
        dto.setProduct(modelMapper.map(review.getProduct(), ProductDTO.class));
        dto.setStars(review.getStars());
        dto.setReviewText(review.getReviewText());
        dto.setReviewDate(review.getReviewDate());
        return dto;
    }
}
