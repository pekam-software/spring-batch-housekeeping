package org.pekam.springbatch.housekeeping.metadata.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus.COMPLETED;
import static org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus.FAILED;

class TableStatsTest {

    @Test
    void init() {
        // given - when
        TableStats initialStats = TableStats.init();

        // then
        assertThat(initialStats.getTotalDeleted()).isEqualTo(0);
        assertTrue(initialStats.getStats().isEmpty());
    }

    @Test
    void shouldUpdateDeletedStatsNoStatusGiven() {
        // given
        TableStats initialStats = TableStats.init();

        // when
        initialStats.updateDeletedStats(123);

        // then
        assertThat(initialStats.getTotalDeleted()).isEqualTo(123);
        assertTrue(initialStats.getStats().isEmpty());
    }

    @Test
    void shouldUpdateDeletedStatsWithStatusGiven() {
        // given
        TableStats initialStats = TableStats.init();

        // when
        initialStats.updateDeletedStats(COMPLETED, 13);
        initialStats.updateDeletedStats(COMPLETED, 17);
        initialStats.updateDeletedStats(COMPLETED, 121);

        initialStats.updateDeletedStats(FAILED, 1);
        initialStats.updateDeletedStats(FAILED, 12);
        initialStats.updateDeletedStats(FAILED, 5);

        // then
        assertThat(initialStats.getTotalDeleted()).isEqualTo(169);
        assertFalse(initialStats.getStats().isEmpty());
        assertThat(initialStats.getStats().get(COMPLETED)).isEqualTo(151);
        assertThat(initialStats.getStats().get(FAILED)).isEqualTo(18);
    }

    @Test
    void shouldUpdateDeletedStatsWithStatusGivenAndNoStatusGiven() {
        // given
        TableStats initialStats = TableStats.init();

        // when
        initialStats.updateDeletedStats(COMPLETED, 11);
        initialStats.updateDeletedStats(COMPLETED, 12);
        initialStats.updateDeletedStats(COMPLETED, 13);

        initialStats.updateDeletedStats(FAILED, 7);
        initialStats.updateDeletedStats(FAILED, 18);
        initialStats.updateDeletedStats(FAILED, 6);

        initialStats.updateDeletedStats(32);

        // then
        assertThat(initialStats.getTotalDeleted()).isEqualTo(99);
        assertFalse(initialStats.getStats().isEmpty());
        assertThat(initialStats.getStats().get(COMPLETED)).isEqualTo(36);
        assertThat(initialStats.getStats().get(FAILED)).isEqualTo(31);
    }

}