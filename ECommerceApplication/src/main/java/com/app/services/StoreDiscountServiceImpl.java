package com.app.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.StoreDiscount;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.StoreDiscountDTO;
import com.app.repositories.ProductRepo;
import com.app.repositories.StoreDiscountRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class StoreDiscountServiceImpl implements StoreDiscountService {

    @Autowired
    private StoreDiscountRepo storeDiscountRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StoreDiscountDTO createStoreDiscount(StoreDiscountDTO storeDiscountDTO) {
        StoreDiscount storeDiscount = modelMapper.map(storeDiscountDTO, StoreDiscount.class);
        StoreDiscount savedDiscount = storeDiscountRepo.save(storeDiscount);
        return modelMapper.map(savedDiscount, StoreDiscountDTO.class);
    }

    @Override
    public List<StoreDiscountDTO> getAllStoreDiscounts() {
        List<StoreDiscount> discounts = storeDiscountRepo.findAll();
        return discounts.stream().map(discount -> modelMapper.map(discount, StoreDiscountDTO.class)).toList();
    }

    @Override
    public StoreDiscountDTO addProductToDiscount(Long discountId, Long productId) {
        StoreDiscount discount = storeDiscountRepo.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("StoreDiscount", "discountId", discountId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (discount.getProducts().contains(product)) {
            throw new APIException("Product already applied to this discount");
        }

        discount.getProducts().add(product);
        product.getStoreDiscounts().add(discount);

        storeDiscountRepo.save(discount);
        productRepo.save(product);

        return modelMapper.map(discount, StoreDiscountDTO.class);
    }
}
