package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.Seller;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Long> {
}
