package com.app.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.entites.Product;
import com.app.entites.StoreDiscount;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceTest {

    @InjectMocks
    private DiscountServiceImpl discountService;

    @Test
    public void testCalculatePrice_WithActiveStoreDiscount() {
        // Arrange
        Product product = new Product();
        product.setPrice(100.0);
        product.setDiscount(10.0); // 10% product discount

        StoreDiscount storeDiscount = new StoreDiscount();
        storeDiscount.setDiscountPercentage(50.0); // 50% store discount
        storeDiscount.setStartDate(LocalDate.now().minusDays(1));
        storeDiscount.setEndDate(LocalDate.now().plusDays(1));

        List<StoreDiscount> discounts = new ArrayList<>();
        discounts.add(storeDiscount);
        product.setStoreDiscounts(discounts);

        // Act
        double price = discountService.calculateProductPrice(product);

        // Assert
        // Should apply 50% discount: 100 - 50 = 50
        assertThat(price).isEqualTo(50.0);
    }

    @Test
    public void testCalculatePrice_WithExpiredStoreDiscount() {
        // Arrange
        Product product = new Product();
        product.setPrice(100.0);
        product.setDiscount(10.0); // 10% product discount

        StoreDiscount storeDiscount = new StoreDiscount();
        storeDiscount.setDiscountPercentage(50.0);
        storeDiscount.setStartDate(LocalDate.now().minusDays(5));
        storeDiscount.setEndDate(LocalDate.now().minusDays(1)); // Expired

        List<StoreDiscount> discounts = new ArrayList<>();
        discounts.add(storeDiscount);
        product.setStoreDiscounts(discounts);

        // Act
        double price = discountService.calculateProductPrice(product);

        // Assert
        // Should fall back to 10% product discount: 100 - 10 = 90
        assertThat(price).isEqualTo(90.0);
    }

    @Test
    public void testCalculatePrice_NoStoreDiscount() {
        // Arrange
        Product product = new Product();
        product.setPrice(100.0);
        product.setDiscount(20.0);

        // Act
        double price = discountService.calculateProductPrice(product);

        // Assert
        assertThat(price).isEqualTo(80.0);
    }
}
