package org.pekam.springbatch.housekeeping.metadata.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableStats {

    private final Map<ExecutionStatus, Integer> stats = new HashMap<>();
    private int totalDeleted = 0;

    public static TableStats init() {
        return new TableStats();
    }

    public void updateDeletedStats(ExecutionStatus executionStatus, int rowsCount) {
        if (stats.containsKey(executionStatus)) {
            stats.put(executionStatus, stats.get(executionStatus) + rowsCount);
        } else {
            stats.put(executionStatus, rowsCount);
        }

        totalDeleted += rowsCount;
    }

    public void updateDeletedStats(int rowsCount) {
        totalDeleted += rowsCount;
    }

}
