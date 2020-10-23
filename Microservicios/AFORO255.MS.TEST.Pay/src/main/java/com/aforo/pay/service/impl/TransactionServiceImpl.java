package com.aforo.pay.service.impl;

import com.aforo.pay.dao.TransactionDao;
import com.aforo.pay.domain.Transaction;
import com.aforo.pay.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private TransactionDao transactionDao;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public Transaction findById(Integer id) {
        return transactionDao.findById(id).orElse(null);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionDao.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return (List<Transaction>) transactionDao.findAll();
    }
}
