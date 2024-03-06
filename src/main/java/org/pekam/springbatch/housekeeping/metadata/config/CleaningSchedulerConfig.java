package org.pekam.springbatch.housekeeping.metadata.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Getter
public class CleaningSchedulerConfig {

    private final CleaningServiceConfig cleaningServiceConfig;

    @Value("${spring.batch.housekeeping.metadata-tables.scheduler.shedlock-table.prefix}")
    private final String shedlockTablePrefix;

    public boolean isSpringBatchMetadataTablesCleaningEnabled() {
        return cleaningServiceConfig.isSpringBatchMetadataTablesCleaningEnabled();
    }

}
