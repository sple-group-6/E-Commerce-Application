package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    List<Review> findByProductProductId(Long productId);

    Review findByUserEmailAndProductProductId(String email, Long productId);

    @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.order.email = ?1 AND oi.product.productId = ?2")
    boolean hasUserPurchasedProduct(String email, Long productId);
}
