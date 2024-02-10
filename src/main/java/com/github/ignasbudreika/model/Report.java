package com.github.ignasbudreika.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record Report(UUID id, BigDecimal revenue, int customers, List<String> items, List<LocalDate> dates) {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Transactions report\n");
        builder.append(String.format("Total Revenue: %s\n", revenue.toString()));
        builder.append(String.format("Unique Customers: %d\n", customers));
        builder.append(String.format("Most Popular Item(s): %s\n",
                                     String.join(", ", items)));
        builder.append(String.format("Date(s) with Highest Revenue: %s",
                                     dates.stream()
                                          .map(LocalDate::toString)
                                          .collect(Collectors.joining(", "))));

        return builder.toString();
    }
}
