package com.github.ignasbudreika.service;

import com.github.ignasbudreika.model.Report;
import com.github.ignasbudreika.model.Transaction;
import com.github.ignasbudreika.repository.ReportRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report calculateReport(List<Transaction> transactions) {
        BigDecimal totalRevenue = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        Map<String, Boolean> customers = new HashMap<>();

        Map<String, Integer> itemFrequencies = new HashMap<>();
        Map<String, Boolean> items = new HashMap<>();
        Integer highestItemFrequency = 0;

        Map<LocalDate, BigDecimal> dailyRevenues = new HashMap<>();
        Map<LocalDate, Boolean> dates = new HashMap<>();
        BigDecimal highestDailyRevenue = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            BigDecimal transactionRevenue = transaction.itemPrice()
                                                       .multiply(new BigDecimal(transaction.itemQuantity()))
                                                       .setScale(2, RoundingMode.HALF_UP);
            totalRevenue = totalRevenue.add(transactionRevenue);

            highestItemFrequency = evaluateFrequency(transaction,
                                                     itemFrequencies,
                                                     items,
                                                     highestItemFrequency);
            highestDailyRevenue = evaluateDailyRevenue(transaction,
                                                       transactionRevenue,
                                                       dailyRevenues,
                                                       dates,
                                                       highestDailyRevenue);

            customers.put(transaction.customerId(), Boolean.TRUE);
        }

        return new Report(UUID.randomUUID(),
                          totalRevenue,
                          customers.size(),
                          items.keySet().stream().toList(),
                          dates.keySet().stream().toList());
    }

    public void saveReport(Report report){
        try {
            reportRepository.save(report);
        } catch (IOException e) {
            System.out.printf("ERROR Unable to save %s report: %s\n", report.id(), e.getMessage());
        }
    }

    private Integer evaluateFrequency(Transaction transaction,
                                      Map<String, Integer> itemFrequencies,
                                      Map<String, Boolean> items,
                                      Integer highestItemFrequency) {
        Integer frequency = itemFrequencies.get(transaction.itemId());
        if (frequency == null) {
            frequency = 1;
            itemFrequencies.put(transaction.itemId(), frequency);
        } else {
            frequency += 1;
            itemFrequencies.put(transaction.itemId(),  frequency);
        }

        if (items.isEmpty() || frequency.equals(highestItemFrequency)) {
            items.put(transaction.itemId(), Boolean.TRUE);
        } else if (frequency.compareTo(highestItemFrequency) > 0) {
            items.clear();
            items.put(transaction.itemId(), Boolean.TRUE);
            highestItemFrequency = frequency;
        }

        return highestItemFrequency;
    }

    private BigDecimal evaluateDailyRevenue(Transaction transaction,
                                            BigDecimal transactionRevenue,
                                            Map<LocalDate, BigDecimal> dailyRevenues,
                                            Map<LocalDate, Boolean> dates,
                                            BigDecimal highestDailyRevenue) {
        BigDecimal revenue = dailyRevenues.get(transaction.date());
        if (revenue == null) {
            revenue = transactionRevenue;
            dailyRevenues.put(transaction.date(), revenue);
        } else {
            revenue = revenue.add(transactionRevenue);
            dailyRevenues.put(transaction.date(), revenue);
        }

        if (dates.isEmpty() || revenue.equals(highestDailyRevenue)) {
            dates.put(transaction.date(), Boolean.TRUE);
        } else if (revenue.compareTo(highestDailyRevenue) > 0) {
            dates.clear();
            dates.put(transaction.date(), Boolean.TRUE);
            highestDailyRevenue = revenue;
        }

        return highestDailyRevenue;
    }
}
