package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.CartDTO;
import com.app.payloads.WishlistDTO;
import com.app.services.WishlistService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/public/wishlists/{wishlistId}/products/{productId}")
    public ResponseEntity<WishlistDTO> addProductToWishlist(@PathVariable Long wishlistId,
            @PathVariable Long productId) {
        WishlistDTO wishlistDTO = wishlistService.addProductToWishlist(wishlistId, productId);

        return new ResponseEntity<WishlistDTO>(wishlistDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/users/{email}/wishlists/{wishlistId}")
    public ResponseEntity<WishlistDTO> getWishlist(@PathVariable String email, @PathVariable Long wishlistId) {
        WishlistDTO wishlistDTO = wishlistService.getWishlist(email, wishlistId);

        return new ResponseEntity<WishlistDTO>(wishlistDTO, HttpStatus.FOUND);
    }

    @DeleteMapping("/public/wishlists/{wishlistId}/products/{productId}")
    public ResponseEntity<String> removeProductFromWishlist(@PathVariable Long wishlistId,
            @PathVariable Long productId) {
        String status = wishlistService.removeProductFromWishlist(wishlistId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

    @PostMapping("/public/wishlists/{wishlistId}/products/{productId}/move-to-cart")
    public ResponseEntity<CartDTO> moveToCart(@PathVariable Long wishlistId, @PathVariable Long productId) {
        CartDTO cartDTO = wishlistService.moveToCart(wishlistId, productId);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }
}
