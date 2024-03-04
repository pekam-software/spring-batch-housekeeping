package org.pekam.springbatch.housekeeping.metadata.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus;
import org.pekam.springbatch.housekeeping.metadata.constants.Table;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MetadataStats {

    private final Map<Table, TableStats> stats = new HashMap<>();
    private int totalDeleted = 0;

    public static MetadataStats init() {
        return new MetadataStats();
    }

    public void updateDeletedStats(Table table, ExecutionStatus executionStatus, int rowsCount) {
        if (stats.containsKey(table)) {
            stats.get(table).updateDeletedStats(executionStatus, rowsCount);
        } else {
            var tableStats = TableStats.init();
            tableStats.updateDeletedStats(executionStatus, rowsCount);

            stats.put(table, tableStats);
        }

        totalDeleted += rowsCount;
    }

    public void updateDeletedStats(Table table, int rowsCount) {
        if (stats.containsKey(table)) {
            stats.get(table).updateDeletedStats(rowsCount);
        } else {
            var tableStats = TableStats.init();
            tableStats.updateDeletedStats(rowsCount);

            stats.put(table, tableStats);
        }

        totalDeleted += rowsCount;
    }

}
