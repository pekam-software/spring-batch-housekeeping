package org.pekam.springbatch.housekeeping.metadata.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.pekam.springbatch.housekeeping.metadata.persistence.SpringBatchMetadataTableService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@ComponentScan("org.pekam.springbatch.housekeeping.metadata")
@ConditionalOnProperty(value = "spring.batch.housekeeping.metadata-tables.enabled", havingValue = "true")
public class DatabaseCleaningScheduler {

    @Value("${spring.batch.housekeeping.metadata-tables.prefix}")
    private final String springBatchTablesPrefix;
    @Value("${spring.batch.housekeeping.metadata-tables.scheduler.shedlock-table.prefix}")
    private final String shedlockTablePrefix;
    @Value("${spring.batch.housekeeping.metadata-tables.enabled}")
    private final Boolean springBatchMetadataTablesCleaningEnabled;
    @Value("${spring.batch.housekeeping.metadata-tables.retention-days}")
    private final Integer springBatchMetadataTablesRetentionDays;
    private final SpringBatchMetadataTableService springBatchMetadataTableService;

    @Transactional
    @Scheduled(cron = "${spring.batch.housekeeping.metadata-tables.scheduler.cron}")
    @SchedulerLock(
            name = "scheduledSpringBatchMetadataTablesCleaning",
            lockAtMostFor = "PT30M"
    )
    public void scheduledCleanup() {
        log.info("[SPRING BATCH TABLES HOUSEKEEPING] Starting scheduled Spring Batch metadata tables cleaning.");

        cleanSpringBatchMetadata();

        log.info("[SPRING BATCH TABLES HOUSEKEEPING] Finished scheduled Spring Batch metadata tables cleaning.");
    }

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                dataSource,
                String.format("%sshedlock", shedlockTablePrefix)
        );
    }

    private void cleanSpringBatchMetadata() {
        if (springBatchMetadataTablesCleaningEnabled) {
            springBatchMetadataTableService.cleanMetadataTables(
                    springBatchTablesPrefix,
                    springBatchMetadataTablesRetentionDays
            );
        }
    }

}
