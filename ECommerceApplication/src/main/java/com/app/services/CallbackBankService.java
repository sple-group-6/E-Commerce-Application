package com.app.services;

import com.app.payloads.CallbackBankDTO;

public interface CallbackBankService {
    CallbackBankDTO pay(Long id, Long paymentId, Double paymentValue);
}
