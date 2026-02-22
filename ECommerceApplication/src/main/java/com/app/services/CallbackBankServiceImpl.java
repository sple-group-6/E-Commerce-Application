package com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Order;
import com.app.entites.PaymentBank;
import com.app.entites.PaymentBankStatus;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CallbackBankDTO;
import com.app.repositories.OrderRepo;
import com.app.repositories.PaymentBankRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CallbackBankServiceImpl implements CallbackBankService {

    @Autowired
    private PaymentBankRepo paymentBankRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public CallbackBankDTO pay(Long id, Long paymentId, Double paymentValue) {
        PaymentBank paymentBank = paymentBankRepo.findByIdAndPaymentPaymentId(id, paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentBank", "id", id));

        if (paymentBank.getStatus() != PaymentBankStatus.PENDING) {
            throw new APIException("Payment is not in pending status");
        }

        if (!paymentBank.getValue().equals(paymentValue)) {
            throw new APIException("Payment value does not match");
        }

        paymentBank.setStatus(PaymentBankStatus.SUCCESS);
        paymentBankRepo.save(paymentBank);

        Order order = paymentBank.getPayment().getOrder();
        order.setOrderStatus("Payment Confirmed");
        orderRepo.save(order);

        return new CallbackBankDTO(paymentId, id, paymentBank.getValue());
    }
}
