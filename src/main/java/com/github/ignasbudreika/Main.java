package com.github.ignasbudreika;

import com.github.ignasbudreika.repository.ReportRepository;
import com.github.ignasbudreika.repository.TransactionRepository;
import com.github.ignasbudreika.service.ReportService;
import com.github.ignasbudreika.service.TransactionService;
import com.github.ignasbudreika.util.FileUtil;

public class Main {

    public static void main(String[] args) {
        FileUtil fileUtil = new FileUtil();

        TransactionRepository transactionRepository = new TransactionRepository(fileUtil);
        ReportRepository reportRepository = new ReportRepository(fileUtil);

        ReportService reportService = new ReportService(reportRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, reportService);

        if (args.length != 1 ||!args[0].startsWith("--transactions=")) {
            System.out.println("INFO Transactions file location not provided");
            return;
        }
        transactionService.processTransactions(args[0].replaceFirst("--transactions=", ""));
    }
}