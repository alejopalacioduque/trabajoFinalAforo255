package com.aforo.pay.dao;

import com.aforo.pay.domain.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionDao extends CrudRepository<Transaction, Integer> {
}
