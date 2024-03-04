package org.pekam.springbatch.housekeeping.metadata.persistence;

import org.pekam.springbatch.housekeeping.metadata.model.MetadataStats;

public interface SpringBatchMetadataTableService {

    MetadataStats cleanMetadataTables(String springBatchTablesPrefix, int retentionDays);

}
