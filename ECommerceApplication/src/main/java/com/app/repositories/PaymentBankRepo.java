package com.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.PaymentBank;

@Repository
public interface PaymentBankRepo extends JpaRepository<PaymentBank, Long> {

    Optional<PaymentBank> findByIdAndPaymentPaymentId(Long id, Long paymentId);

}
