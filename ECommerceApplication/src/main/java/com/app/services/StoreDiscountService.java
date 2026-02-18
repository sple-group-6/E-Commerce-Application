package com.app.services;

import java.util.List;

import com.app.payloads.StoreDiscountDTO;

public interface StoreDiscountService {
    StoreDiscountDTO createStoreDiscount(StoreDiscountDTO storeDiscountDTO);

    List<StoreDiscountDTO> getAllStoreDiscounts();

    StoreDiscountDTO addProductToDiscount(Long discountId, Long productId);
}
