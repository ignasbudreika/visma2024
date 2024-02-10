package service;

import com.github.ignasbudreika.model.Report;
import com.github.ignasbudreika.model.Transaction;
import com.github.ignasbudreika.repository.ReportRepository;
import com.github.ignasbudreika.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReportServiceTest {

    private static final LocalDate first = LocalDate.of(1970, 1, 1);
    private static final LocalDate second = LocalDate.of(1970, 1, 2);

    private final ReportRepository reportRepository = mock(ReportRepository.class);

    private final ReportService target = new ReportService(reportRepository);

    @Test
    void shouldCalculateReport() {
        Transaction tx1 = new Transaction("tx1", "customer1", "item1", first, BigDecimal.TEN, 2);
        Transaction tx2 = new Transaction("tx2", "customer1", "item2", first, BigDecimal.TEN, 2);
        Transaction tx3 = new Transaction("tx3", "customer2", "item3", first, BigDecimal.TEN, 2);
        Transaction tx4 = new Transaction("tx4", "customer3", "item1", second, BigDecimal.TEN, 2);
        List<Transaction> transactions = List.of(tx1, tx2, tx3, tx4);

        Report result = target.calculateReport(transactions);

        assertEquals(new BigDecimal("80.00"), result.revenue());
        assertEquals(3, result.customers());
        assertEquals("item1", result.items().get(0));
        assertEquals(first, result.dates().get(0));
    }

    @Test
    void shouldSaveReport() throws IOException {
        Report report = new Report(UUID.randomUUID(), BigDecimal.TEN, 2, new ArrayList<>(), new ArrayList<>());

        target.saveReport(report);

        ArgumentCaptor<Report> captor = ArgumentCaptor.forClass(Report.class);
        verify(reportRepository).save(captor.capture());

        Report captured = captor.getValue();
        assertNotNull(captured);
        assertEquals(report.id(), captured.id());
        assertEquals(report.revenue(), captured.revenue());
        assertEquals(report.customers(), captured.customers());
        assertEquals(report.items(), captured.items());
        assertEquals(report.dates(), captured.dates());
    }
}
