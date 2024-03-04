package org.pekam.springbatch.housekeeping.metadata.persistence;

import org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus;
import org.pekam.springbatch.housekeeping.metadata.constants.Table;

public class DeleteQueryFactory {

    private static final String EMPTY_STRING = "";
    private static final String CREATE_DATE_CONDITION = " AND CREATE_TIME <= ?";
    private static final String DEFAULT_TABLES_PREFIX = "BATCH_";

    public static String get(
            String springBatchTablesPrefix,
            Table table,
            ExecutionStatus executionStatus,
            boolean withRetentionDays
    ) {
        return String.format(
                table.getDeleteQuery(),
                sanitizePrefix(springBatchTablesPrefix),
                executionStatus,
                withRetentionDays ? CREATE_DATE_CONDITION : EMPTY_STRING
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
