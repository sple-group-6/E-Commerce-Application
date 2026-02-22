package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.WishlistItem;

@Repository
public interface WishlistItemRepo extends JpaRepository<WishlistItem, Long> {

    @Query("SELECT wi FROM WishlistItem wi WHERE wi.wishlist.id = ?1 AND wi.product.id = ?2")
    WishlistItem findWishlistItemByWishlistIdAndProductId(Long wishlistId, Long productId);

    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.wishlist.id = ?1 AND wi.product.id = ?2")
    void deleteWishlistItemByWishlistIdAndProductId(Long wishlistId, Long productId);
}
