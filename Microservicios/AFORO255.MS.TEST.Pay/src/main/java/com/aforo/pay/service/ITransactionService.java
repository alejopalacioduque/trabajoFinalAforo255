package com.aforo.pay.service;

import com.aforo.pay.domain.Transaction;

import java.util.List;

public interface ITransactionService {

    Transaction findById(Integer id);
    Transaction save (Transaction transaction);
    List<Transaction> findAll();
}
