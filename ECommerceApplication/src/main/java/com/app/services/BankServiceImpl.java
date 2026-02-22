package com.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Bank;
import com.app.exceptions.APIException;
import com.app.repositories.BankRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepo bankRepo;

    @Override
    public List<Bank> getBankList() {
        List<Bank> banks = bankRepo.findAll();

        if (banks.isEmpty()) {
            throw new APIException("No banks found");
        }

        return banks;
    }
}
