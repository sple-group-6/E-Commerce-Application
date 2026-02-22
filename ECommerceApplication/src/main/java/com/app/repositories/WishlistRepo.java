package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.Wishlist;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist, Long> {

    @Query("SELECT w FROM Wishlist w WHERE w.user.email = ?1 AND w.id = ?2")
    Wishlist findWishlistByEmailAndWishlistId(String email, Long wishlistId);
}
