package service;

import com.github.ignasbudreika.model.Report;
import com.github.ignasbudreika.model.Transaction;
import com.github.ignasbudreika.repository.TransactionRepository;
import com.github.ignasbudreika.service.ReportService;
import com.github.ignasbudreika.service.TransactionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    private static final String FILE = "file.csv";

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);

    private final ReportService reportService = mock(ReportService.class);

    private final TransactionService target = new TransactionService(transactionRepository, reportService);

    @Test
    void shouldProcessTransactions() {
        when(transactionRepository.loadTransactions(FILE))
                .thenReturn(List.of(new Transaction("tx1", "customer1", "item1", LocalDate.now(), BigDecimal.TEN, 2)));
        when(reportService.calculateReport(anyList()))
                .thenReturn(new Report(UUID.randomUUID(), BigDecimal.TEN, 1, new ArrayList<>(), new ArrayList<>()));

        target.processTransactions(FILE);

        verify(transactionRepository).loadTransactions(FILE);
        verify(reportService).calculateReport(anyList());
        verify(reportService).saveReport(any(Report.class));
    }

    @Test
    void shouldSkipReport_whenNoTransactionsAreFound() {
        when(transactionRepository.loadTransactions(FILE)).thenReturn(new ArrayList<>());

        target.processTransactions(FILE);

        verify(transactionRepository).loadTransactions(FILE);
        verify(reportService, never()).calculateReport(anyList());
        verify(reportService, never()).saveReport(any(Report.class));
    }
}
