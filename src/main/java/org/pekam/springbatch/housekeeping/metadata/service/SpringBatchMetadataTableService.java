package org.pekam.springbatch.housekeeping.metadata.service;

import org.pekam.springbatch.housekeeping.metadata.config.CleaningServiceConfig;
import org.pekam.springbatch.housekeeping.metadata.model.MetadataStats;

public interface SpringBatchMetadataTableService {

    MetadataStats cleanMetadataTables(CleaningServiceConfig cleaningServiceConfig);

}
