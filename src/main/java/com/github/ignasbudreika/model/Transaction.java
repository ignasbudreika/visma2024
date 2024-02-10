package com.github.ignasbudreika.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;

public record Transaction(String id, String customerId, String itemId, LocalDate date, BigDecimal itemPrice, int itemQuantity) {

    public Transaction(String[] data) throws ParseException {
        this(data[0],
             data[1],
             data[2],
             LocalDate.parse(data[3]),
             new BigDecimal(data[4]),
             Integer.parseInt(data[5]));
    }
}
