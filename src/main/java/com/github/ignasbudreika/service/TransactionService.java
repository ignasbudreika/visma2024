package com.github.ignasbudreika.service;

import com.github.ignasbudreika.model.Report;
import com.github.ignasbudreika.model.Transaction;
import com.github.ignasbudreika.repository.TransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TransactionService {

    protected static final Logger logger = LogManager.getLogger();

    private final TransactionRepository transactionRepository;

    private final ReportService reportService;

    public TransactionService(TransactionRepository transactionRepository, ReportService reportService) {
        this.transactionRepository = transactionRepository;
        this.reportService = reportService;
    }

    public void processTransactions(String transactionsFile) {
        List<Transaction> transactions = transactionRepository.loadTransactions(transactionsFile);
        if (transactions.isEmpty()) {
            logger.info("No transactions found, skipping report calculation");
            return;
        }

        Report report = reportService.calculateReport(transactions);

        reportService.saveReport(report);
    }
}
