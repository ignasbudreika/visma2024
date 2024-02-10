package com.github.ignasbudreika.service;

import com.github.ignasbudreika.model.Report;
import com.github.ignasbudreika.model.Transaction;
import com.github.ignasbudreika.repository.TransactionRepository;

import java.util.List;

public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final ReportService reportService;

    public TransactionService(TransactionRepository transactionRepository, ReportService reportService) {
        this.transactionRepository = transactionRepository;
        this.reportService = reportService;
    }

    public void processTransactions(String transactionsFile) {
        List<Transaction> transactions = transactionRepository.loadTransactions(transactionsFile);
        if (transactions.isEmpty()) {
            System.out.println("INFO No transactions found, skipping report calculation");
            return;
        }

        Report report = reportService.calculateReport(transactions);

        reportService.saveReport(report);
    }
}
