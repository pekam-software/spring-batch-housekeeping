package org.pekam.springbatch.housekeeping.metadata.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Getter
public class CleaningServiceConfig {

    @Value("${spring.batch.housekeeping.metadata-tables.prefix}")
    private final String springBatchTablesPrefix;
    @Value("${spring.batch.housekeeping.metadata-tables.cleaning-enabled-for-completed}")
    private final Boolean springBatchMetadataTablesCleaningEnabledForCompleted;
    @Value("${spring.batch.housekeeping.metadata-tables.cleaning-enabled-for-failed}")
    private final Boolean springBatchMetadataTablesCleaningEnabledForFailed;
    @Value("${spring.batch.housekeeping.metadata-tables.retention-days-for-completed}")
    private final Integer springBatchMetadataTablesRetentionDaysForCompleted;
    @Value("${spring.batch.housekeeping.metadata-tables.retention-days-for-failed}")
    private final Integer springBatchMetadataTablesRetentionDaysForFailed;

    public boolean isSpringBatchMetadataTablesCleaningEnabled() {
        return springBatchMetadataTablesCleaningEnabledForCompleted ||
                springBatchMetadataTablesCleaningEnabledForFailed;
    }

}
