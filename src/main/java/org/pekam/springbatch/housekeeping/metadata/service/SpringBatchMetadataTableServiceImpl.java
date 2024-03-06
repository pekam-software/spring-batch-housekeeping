package org.pekam.springbatch.housekeeping.metadata.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pekam.springbatch.housekeeping.metadata.config.CleaningServiceConfig;
import org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus;
import org.pekam.springbatch.housekeeping.metadata.constants.Table;
import org.pekam.springbatch.housekeeping.metadata.model.MetadataStats;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus.COMPLETED;
import static org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus.FAILED;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_JOB_EXECUTION;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_JOB_EXECUTION_CONTEXT;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_JOB_EXECUTION_PARAMS;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_JOB_INSTANCE;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_STEP_EXECUTION;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_STEP_EXECUTION_CONTEXT;
import static org.pekam.springbatch.housekeeping.metadata.utils.DateUtils.addDays;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpringBatchMetadataTableServiceImpl implements SpringBatchMetadataTableService {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
    private static final String LOG_PREFIX = "[SPRING BATCH TABLES HOUSEKEEPING]";

    private final JdbcTemplate jdbcTemplate;


    @Override
    public MetadataStats cleanMetadataTables(CleaningServiceConfig cleaningServiceConfig) {
        Assert.notNull(jdbcTemplate, "The jdbcTemplate must not be null.");

        final Date dateForCompleted = getDateForCompleted(cleaningServiceConfig);
        final Date dateForFailed = getDateForFailed(cleaningServiceConfig);

        printInitialLogs(cleaningServiceConfig, dateForCompleted, dateForFailed);

        final var metadataStats = MetadataStats.init();

        cleanTable(cleaningServiceConfig, BATCH_STEP_EXECUTION_CONTEXT, metadataStats, dateForCompleted, dateForFailed);
        cleanTable(cleaningServiceConfig, BATCH_STEP_EXECUTION, metadataStats, dateForCompleted, dateForFailed);
        cleanTable(cleaningServiceConfig, BATCH_JOB_EXECUTION_CONTEXT, metadataStats, dateForCompleted, dateForFailed);
        cleanTable(cleaningServiceConfig, BATCH_JOB_EXECUTION_PARAMS, metadataStats, dateForCompleted, dateForFailed);
        cleanTable(cleaningServiceConfig, BATCH_JOB_EXECUTION, metadataStats, dateForCompleted, dateForFailed);
        cleanTable(cleaningServiceConfig, BATCH_JOB_INSTANCE, metadataStats);

        log.info(
                "{} Deleted {} rows in total from the Spring Batch metadata tables",
                LOG_PREFIX,
                metadataStats.getTotalDeleted()
        );

        return metadataStats;
    }

    private void cleanTable(
            CleaningServiceConfig cleaningServiceConfig,
            Table table,
            MetadataStats metadataStats,
            Date dateForCompleted,
            Date dateForFailed
    ) {
        if (cleaningServiceConfig.getSpringBatchMetadataTablesCleaningEnabledForCompleted()) {
            cleanTable(
                    cleaningServiceConfig.getSpringBatchTablesPrefix(),
                    table,
                    COMPLETED,
                    metadataStats,
                    dateForCompleted
            );
        }

        if (cleaningServiceConfig.getSpringBatchMetadataTablesCleaningEnabledForFailed()) {
            cleanTable(
                    cleaningServiceConfig.getSpringBatchTablesPrefix(),
                    table,
                    FAILED,
                    metadataStats,
                    dateForFailed
            );
        }
    }

    private void cleanTable(
            String springBatchTablesPrefix,
            Table table,
            ExecutionStatus executionStatus,
            MetadataStats metadataStats,
            Date date
    ) {
        int rowsCount = jdbcTemplate.update(
                DeleteQueryFactory.get(springBatchTablesPrefix, table, executionStatus),
                date
        );
        metadataStats.updateDeletedStats(table, executionStatus, rowsCount);

        log.info(
                "{} Deleted {} rows with {} status from the {} table.",
                LOG_PREFIX,
                rowsCount,
                executionStatus.name(),
                table
        );
    }

    private void cleanTable(
            CleaningServiceConfig cleaningServiceConfig,
            Table table,
            MetadataStats metadataStats
    ) {
        int rowsCount = jdbcTemplate.update(
                DeleteQueryFactory.get(cleaningServiceConfig.getSpringBatchTablesPrefix(), table)
        );
        metadataStats.updateDeletedStats(table, rowsCount);

        log.info(
                "{} Deleted {} rows from the {} table.",
                LOG_PREFIX,
                rowsCount,
                table
        );
    }

    private Date getDateForCompleted(CleaningServiceConfig cleaningServiceConfig) {
        return addDays(
                new Date(),
                -cleaningServiceConfig.getSpringBatchMetadataTablesRetentionDaysForCompleted()
        );
    }

    private Date getDateForFailed(CleaningServiceConfig cleaningServiceConfig) {
        return addDays(
                new Date(),
                -cleaningServiceConfig.getSpringBatchMetadataTablesRetentionDaysForFailed()
        );
    }

    private void printInitialLogs(
            CleaningServiceConfig cleaningServiceConfig,
            Date dateForCompleted,
            Date dateForFailed
    ) {
        log.info("{} Removing the Spring Batch history.", LOG_PREFIX);

        if (cleaningServiceConfig.getSpringBatchMetadataTablesCleaningEnabledForCompleted()) {
            log.info(
                    "{} All rows with COMPLETED status will be deleted when older than {}.",
                    LOG_PREFIX,
                    DATE_FORMAT.format(dateForCompleted)
            );
        }

        if (cleaningServiceConfig.getSpringBatchMetadataTablesCleaningEnabledForFailed()) {
            log.info(
                    "{} All rows with FAILED status will be deleted when older than {}.",
                    LOG_PREFIX,
                    DATE_FORMAT.format(dateForFailed)
            );
        }
    }

}
