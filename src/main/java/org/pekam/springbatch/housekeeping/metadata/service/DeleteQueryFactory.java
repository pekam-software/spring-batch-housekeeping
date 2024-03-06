package org.pekam.springbatch.housekeeping.metadata.service;

import org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus;
import org.pekam.springbatch.housekeeping.metadata.constants.Table;

public class DeleteQueryFactory {

    private static final String CREATE_DATE_CONDITION = "AND CREATE_TIME <= ?";
    private static final String DEFAULT_TABLES_PREFIX = "BATCH_";

    public static String get(
            String springBatchTablesPrefix,
            Table table,
            ExecutionStatus executionStatus
    ) {
        return String.format(
                table.getDeleteQuery(),
                sanitizePrefix(springBatchTablesPrefix),
                executionStatus,
                CREATE_DATE_CONDITION
        );
    }

    public static String get(String springBatchTablesPrefix, Table table) {
        return String.format(
                table.getDeleteQuery(),
                sanitizePrefix(springBatchTablesPrefix)
        );
    }

    private static String sanitizePrefix(String springBatchTablesPrefix) {
        var sanitizedPrefix = springBatchTablesPrefix;

        if (isEmpty(springBatchTablesPrefix)) {
            sanitizedPrefix = DEFAULT_TABLES_PREFIX;
        }

        return sanitizedPrefix;
    }

    private static boolean isEmpty(String springBatchTablesPrefix) {
        return springBatchTablesPrefix == null ||
                springBatchTablesPrefix.isEmpty() ||
                springBatchTablesPrefix.isBlank();
    }

}
