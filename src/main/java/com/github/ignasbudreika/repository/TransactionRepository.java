package com.github.ignasbudreika.repository;

import com.github.ignasbudreika.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> loadTransactions(String path);
}
