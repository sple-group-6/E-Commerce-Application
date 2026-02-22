package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.Discount;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, Long> {

    boolean existsByCode(String code);

    java.util.Optional<Discount> findByCode(String code);
}
