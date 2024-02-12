package com.github.ignasbudreika;

import com.github.ignasbudreika.repository.ReportRepository;
import com.github.ignasbudreika.repository.ReportRepositoryFileImpl;
import com.github.ignasbudreika.repository.TransactionRepository;
import com.github.ignasbudreika.repository.TransactionRepositoryFileImpl;
import com.github.ignasbudreika.service.ReportService;
import com.github.ignasbudreika.service.TransactionService;
import com.github.ignasbudreika.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    protected static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        FileUtil fileUtil = new FileUtil();

        TransactionRepository transactionRepository = new TransactionRepositoryFileImpl(fileUtil);
        ReportRepository reportRepository = new ReportRepositoryFileImpl(fileUtil);

        ReportService reportService = new ReportService(reportRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, reportService);

        if (args.length != 1 ||!args[0].startsWith("--transactions=")) {
            logger.info("Transactions file location not provided");
            return;
        }
        transactionService.processTransactions(args[0].replaceFirst("--transactions=", ""));
    }
}