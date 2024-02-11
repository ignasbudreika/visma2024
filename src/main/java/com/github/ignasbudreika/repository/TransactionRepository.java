package com.github.ignasbudreika.repository;

import com.github.ignasbudreika.model.Transaction;
import com.github.ignasbudreika.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    protected static final Logger logger = LogManager.getLogger();

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
                    logger.warn("Failed to parse transaction", e);
                }
            }

            return txs;
        } catch (IOException e) {
            logger.error("Unable to read transactions", e);

            return new ArrayList<>();
        }
    }
}
