package com.app.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.StoreDiscount;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Override
    public double calculateProductPrice(Product product) {
        double originalPrice = product.getPrice();

        // 1. Check for active Store Discounts
        List<StoreDiscount> storeDiscounts = product.getStoreDiscounts();

        LocalDate today = LocalDate.now();

        Optional<StoreDiscount> bestStoreDiscount = storeDiscounts.stream()
                .filter(discount -> !today.isBefore(discount.getStartDate()) && !today.isAfter(discount.getEndDate()))
                .max(Comparator.comparing(StoreDiscount::getDiscountPercentage));

        if (bestStoreDiscount.isPresent()) {
            double discountPercentage = bestStoreDiscount.get().getDiscountPercentage();
            return originalPrice - (originalPrice * discountPercentage / 100);
        }

        // 2. Fallback to Product Discount (Default)
        double productDiscount = product.getDiscount();
        return originalPrice - (originalPrice * productDiscount / 100);
    }
}
