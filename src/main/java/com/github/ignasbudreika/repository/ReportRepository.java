package com.github.ignasbudreika.repository;

import com.github.ignasbudreika.model.Report;
import com.github.ignasbudreika.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReportRepository {

    protected static final Logger logger = LogManager.getLogger();

    public final FileUtil fileUtil;

    public ReportRepository(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void save(Report report) throws IOException {
        String file = createReportFileName(report);

        fileUtil.writeToFile(report.toString(), file);

        logger.info(String.format("Report saved to: %s", file));
    }

    private String createReportFileName(Report report) {
        StringBuilder builder = new StringBuilder();
        builder.append("report_");
        builder.append(report.id());
        builder.append("_");
        builder.append(LocalDateTime.now()
                                    .truncatedTo(ChronoUnit.MINUTES)
                                    .toString()
                                    .replace(":", "_"));
        builder.append(".txt");

        return builder.toString();
    }
}
