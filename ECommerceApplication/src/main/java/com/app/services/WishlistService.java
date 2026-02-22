package com.app.services;

import com.app.payloads.CartDTO;
import com.app.payloads.WishlistDTO;

public interface WishlistService {

    WishlistDTO addProductToWishlist(Long wishlistId, Long productId);

    WishlistDTO getWishlist(String email, Long wishlistId);

    String removeProductFromWishlist(Long wishlistId, Long productId);

    CartDTO moveToCart(Long wishlistId, Long productId);
}
