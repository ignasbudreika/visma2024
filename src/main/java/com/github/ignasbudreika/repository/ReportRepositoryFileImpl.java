package com.github.ignasbudreika.repository;

import com.github.ignasbudreika.model.Report;
import com.github.ignasbudreika.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReportRepositoryFileImpl implements ReportRepository {

    protected static final Logger logger = LogManager.getLogger();

    public final FileUtil fileUtil;

    public ReportRepositoryFileImpl(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void save(Report report) {
        String file = createReportFileName(report);

        try {
            fileUtil.writeToFile(report.toString(), file);

            logger.info(String.format("Report saved to: %s", file));
        } catch (IOException e) {
            logger.error(String.format("Unable to save %s report", report.id()), e.getMessage());
        }
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
