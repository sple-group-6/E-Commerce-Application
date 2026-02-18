package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.StoreDiscountDTO;
import com.app.services.StoreDiscountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class StoreDiscountController {

    @Autowired
    private StoreDiscountService storeDiscountService;

    @PostMapping("/admin/store-discounts")
    public ResponseEntity<StoreDiscountDTO> createStoreDiscount(@RequestBody StoreDiscountDTO storeDiscountDTO) {
        StoreDiscountDTO savedDiscount = storeDiscountService.createStoreDiscount(storeDiscountDTO);
        return new ResponseEntity<StoreDiscountDTO>(savedDiscount, HttpStatus.CREATED);
    }

    @GetMapping("/store-discounts")
    public ResponseEntity<List<StoreDiscountDTO>> getAllStoreDiscounts() {
        List<StoreDiscountDTO> discounts = storeDiscountService.getAllStoreDiscounts();
        return new ResponseEntity<List<StoreDiscountDTO>>(discounts, HttpStatus.OK);
    }

    @PutMapping("/admin/store-discounts/{discountId}/products/{productId}")
    public ResponseEntity<StoreDiscountDTO> addProductToDiscount(@PathVariable Long discountId,
            @PathVariable Long productId) {
        StoreDiscountDTO discount = storeDiscountService.addProductToDiscount(discountId, productId);
        return new ResponseEntity<StoreDiscountDTO>(discount, HttpStatus.OK);
    }
}
