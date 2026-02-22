package com.app.controllers;

import com.app.entites.Bank;
import com.app.payloads.CallbackBankDTO;
import com.app.payloads.CallbackBankRequestDTO;
import com.app.services.BankService;
import com.app.services.CallbackBankService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class BankController {

    @Autowired
    private BankService bankService;

    @Autowired
    private CallbackBankService callbackBankService;

    @GetMapping("/public/banks")
    public ResponseEntity<List<Bank>> getBankList() {
        List<Bank> banks = bankService.getBankList();
        return new ResponseEntity<>(banks, HttpStatus.OK);
    }

    @PostMapping("/callback/bank")
    public ResponseEntity<CallbackBankDTO> callbackBank(@RequestBody CallbackBankRequestDTO request) {
        CallbackBankDTO response = callbackBankService.pay(
                request.getPaymentBankId(),
                request.getPaymentId(),
                request.getValue()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


