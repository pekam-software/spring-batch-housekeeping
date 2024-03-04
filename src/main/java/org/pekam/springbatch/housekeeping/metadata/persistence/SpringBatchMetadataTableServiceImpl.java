package org.pekam.springbatch.housekeeping.metadata.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private static final DateFormat dateFormat = new SimpleDateFormat();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MetadataStats cleanMetadataTables(String springBatchTablesPrefix, int retentionDays) {
        Assert.notNull(jdbcTemplate, "The jdbcTemplate must not be null.");

        final Date date = addDays(new Date(), -retentionDays);

        log.info(
                "[SPRING BATCH TABLES HOUSEKEEPING] Removing the Spring Batch history using {} retention days.",
                retentionDays
        );
        log.info("[SPRING BATCH TABLES HOUSEKEEPING] All rows with COMPLETED status will be deleted.");
        log.info(
                "[SPRING BATCH TABLES HOUSEKEEPING] All rows with FAILED status will be deleted when older than {}.",
                dateFormat.format(date)
        );

        final var metadataStats = MetadataStats.init();

        cleanTable(springBatchTablesPrefix, BATCH_STEP_EXECUTION_CONTEXT, date, metadataStats);
        cleanTable(springBatchTablesPrefix, BATCH_STEP_EXECUTION, date, metadataStats);
        cleanTable(springBatchTablesPrefix, BATCH_JOB_EXECUTION_CONTEXT, date, metadataStats);
        cleanTable(springBatchTablesPrefix, BATCH_JOB_EXECUTION_PARAMS, date, metadataStats);
        cleanTable(springBatchTablesPrefix, BATCH_JOB_EXECUTION, date, metadataStats);
        cleanTable(springBatchTablesPrefix, BATCH_JOB_INSTANCE, metadataStats);

        log.info(
                "[SPRING BATCH TABLES HOUSEKEEPING] Deleted {} rows in total from the Spring Batch metadata tables",
                metadataStats.getTotalDeleted()
        );

        return metadataStats;
    }

    private void cleanTable(
            String springBatchTablesPrefix,
            Table table,
            Date date,
            MetadataStats metadataStats
    ) {
        int rowsCount = jdbcTemplate.update(
                DeleteQueryFactory.get(springBatchTablesPrefix, table, COMPLETED, false)
        );
        metadataStats.updateDeletedStats(table, COMPLETED, rowsCount);

        log.info(
                "[SPRING BATCH TABLES HOUSEKEEPING] Deleted {} rows with COMPLETED status from the {} table.",
                rowsCount,
                table
        );

        rowsCount = jdbcTemplate.update(
                DeleteQueryFactory.get(springBatchTablesPrefix, table, FAILED, true),
                date
        );
        metadataStats.updateDeletedStats(table, FAILED, rowsCount);

        log.info(
                "[SPRING BATCH TABLES HOUSEKEEPING] Deleted {} rows with FAILED status from the {} table.",
                rowsCount,
                table
        );
    }

    private void cleanTable(
            String springBatchTablesPrefix,
            Table table,
            MetadataStats metadataStats
    ) {
        int rowsCount = jdbcTemplate.update(DeleteQueryFactory.get(springBatchTablesPrefix, table));
        metadataStats.updateDeletedStats(table, rowsCount);

        log.info("[SPRING BATCH TABLES HOUSEKEEPING] Deleted {} rows from the {} table.", rowsCount, table);
    }

}
