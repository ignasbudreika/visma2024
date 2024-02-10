package com.github.ignasbudreika.repository;

import com.github.ignasbudreika.model.Transaction;
import com.github.ignasbudreika.util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    private final FileUtil fileUtil;

    public TransactionRepository(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public List<Transaction> loadTransactions(String path) {
        try {
            BufferedReader reader = fileUtil.getReader(path);

            String line = reader.readLine();

            List<Transaction> txs = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                try {
                    Transaction transaction = new Transaction(line.split(","));
                    txs.add(transaction);
                } catch (ParseException e) {
                    System.out.printf("WARN Failed to parse transaction: %s\n", e.getMessage());
                }
            }

            return txs;
        } catch (IOException e) {
            System.out.printf("ERROR Unable to read transactions: %s\n", e.getMessage());

            return new ArrayList<>();
        }
    }
}
