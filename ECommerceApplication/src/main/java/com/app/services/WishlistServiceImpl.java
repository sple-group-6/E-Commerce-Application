package com.app.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.Wishlist;
import com.app.entites.WishlistItem;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CartDTO;
import com.app.payloads.ProductDTO;
import com.app.payloads.WishlistDTO;
import com.app.payloads.WishlistItemDTO;
import com.app.repositories.ProductRepo;
import com.app.repositories.WishlistItemRepo;
import com.app.repositories.WishlistRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private WishlistItemRepo wishlistItemRepo;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public WishlistDTO addProductToWishlist(Long wishlistId, Long productId) {

        Wishlist wishlist = wishlistRepo.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        WishlistItem existingItem = wishlistItemRepo.findWishlistItemByWishlistIdAndProductId(wishlistId, productId);

        if (existingItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the wishlist");
        }

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setWishlist(wishlist);
        wishlistItem.setProduct(product);
        wishlistItem.setAddedDate(LocalDate.now());

        wishlistItemRepo.save(wishlistItem);

        return mapToWishlistDTO(wishlist);
    }

    @Override
    public WishlistDTO getWishlist(String email, Long wishlistId) {

        Wishlist wishlist = wishlistRepo.findWishlistByEmailAndWishlistId(email, wishlistId);

        if (wishlist == null) {
            throw new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId);
        }

        return mapToWishlistDTO(wishlist);
    }

    @Override
    public String removeProductFromWishlist(Long wishlistId, Long productId) {

        wishlistRepo.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId));

        WishlistItem wishlistItem = wishlistItemRepo.findWishlistItemByWishlistIdAndProductId(wishlistId, productId);

        if (wishlistItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        wishlistItemRepo.deleteWishlistItemByWishlistIdAndProductId(wishlistId, productId);

        return "Product " + wishlistItem.getProduct().getProductName() + " removed from the wishlist !!!";
    }

    @Override
    public CartDTO moveToCart(Long wishlistId, Long productId) {

        Wishlist wishlist = wishlistRepo.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId));

        WishlistItem wishlistItem = wishlistItemRepo.findWishlistItemByWishlistIdAndProductId(wishlistId, productId);

        if (wishlistItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        Long cartId = wishlist.getUser().getCart().getCartId();

        CartDTO cartDTO = cartService.addProductToCart(cartId, productId, 1);

        wishlistItemRepo.deleteWishlistItemByWishlistIdAndProductId(wishlistId, productId);

        return cartDTO;
    }

    private WishlistDTO mapToWishlistDTO(Wishlist wishlist) {
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setWishlistId(wishlist.getWishlistId());

        List<WishlistItemDTO> itemDTOs = wishlist.getWishlistItems().stream().map(item -> {
            WishlistItemDTO itemDTO = new WishlistItemDTO();
            itemDTO.setWishlistItemId(item.getWishlistItemId());
            itemDTO.setProduct(modelMapper.map(item.getProduct(), ProductDTO.class));
            itemDTO.setAddedDate(item.getAddedDate());
            return itemDTO;
        }).collect(Collectors.toList());

        wishlistDTO.setWishlistItems(itemDTOs);

        return wishlistDTO;
    }
}
