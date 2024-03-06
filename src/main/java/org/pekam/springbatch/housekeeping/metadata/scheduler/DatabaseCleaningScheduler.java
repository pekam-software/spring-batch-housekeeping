package org.pekam.springbatch.housekeeping.metadata.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.pekam.springbatch.housekeeping.metadata.config.CleaningSchedulerConfig;
import org.pekam.springbatch.housekeeping.metadata.service.SpringBatchMetadataTableService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
@ConditionalOnExpression(value = "${spring.batch.housekeeping.metadata-tables.cleaning-enabled-for-completed:true} " +
        "or ${spring.batch.housekeeping.metadata-tables.cleaning-enabled-for-failed:true}")
public class DatabaseCleaningScheduler {

    private final CleaningSchedulerConfig cleaningSchedulerConfig;
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
                String.format("%sshedlock", cleaningSchedulerConfig.getShedlockTablePrefix())
        );
    }

    private void cleanSpringBatchMetadata() {
        if (cleaningSchedulerConfig.isSpringBatchMetadataTablesCleaningEnabled()) {
            springBatchMetadataTableService.cleanMetadataTables(
                    cleaningSchedulerConfig.getCleaningServiceConfig()
            );
        }
    }

}
