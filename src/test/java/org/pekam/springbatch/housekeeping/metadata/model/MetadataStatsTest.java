package org.pekam.springbatch.housekeeping.metadata.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus.COMPLETED;
import static org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus.FAILED;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_JOB_EXECUTION;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_JOB_EXECUTION_CONTEXT;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_JOB_INSTANCE;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_STEP_EXECUTION;

class MetadataStatsTest {

    @Test
    void init() {
        // given - when
        MetadataStats initialStats = MetadataStats.init();

        // then
        assertThat(initialStats.getTotalDeleted()).isEqualTo(0);
        assertTrue(initialStats.getStats().isEmpty());
    }

    @Test
    void shouldUpdateDeletedStatsNoStatusGiven() {
        // given
        MetadataStats initialStats = MetadataStats.init();

        // when
        initialStats.updateDeletedStats(BATCH_JOB_EXECUTION, 123);

        // then
        assertThat(initialStats.getTotalDeleted()).isEqualTo(123);
        assertFalse(initialStats.getStats().isEmpty());
        assertTrue(initialStats.getStats().get(BATCH_JOB_EXECUTION).getStats().isEmpty());
        assertThat(initialStats.getStats().get(BATCH_JOB_EXECUTION).getTotalDeleted()).isEqualTo(123);
    }

    @Test
    void shouldUpdateDeletedStatsWithStatusGiven() {
        // given
        MetadataStats initialStats = MetadataStats.init();

        // when
        initialStats.updateDeletedStats(BATCH_JOB_EXECUTION, COMPLETED, 13);
        initialStats.updateDeletedStats(BATCH_JOB_EXECUTION, COMPLETED, 15);
        initialStats.updateDeletedStats(BATCH_JOB_EXECUTION_CONTEXT, COMPLETED,36);
        initialStats.updateDeletedStats(BATCH_JOB_INSTANCE, COMPLETED, 1);

        initialStats.updateDeletedStats(BATCH_JOB_EXECUTION, FAILED, 24);
        initialStats.updateDeletedStats(BATCH_JOB_INSTANCE, FAILED, 8);
        initialStats.updateDeletedStats(BATCH_STEP_EXECUTION, FAILED, 12);
        initialStats.updateDeletedStats(BATCH_STEP_EXECUTION, FAILED, 5);

        // then
        assertThat(initialStats.getTotalDeleted()).isEqualTo(114);
        assertFalse(initialStats.getStats().isEmpty());
        assertThat(initialStats.getStats().get(BATCH_JOB_EXECUTION).getTotalDeleted()).isEqualTo(52);
        assertThat(initialStats.getStats().get(BATCH_JOB_EXECUTION).getStats().get(COMPLETED)).isEqualTo(28);
        assertThat(initialStats.getStats().get(BATCH_JOB_EXECUTION).getStats().get(FAILED)).isEqualTo(24);
        assertThat(initialStats.getStats().get(BATCH_JOB_INSTANCE).getTotalDeleted()).isEqualTo(9);
        assertThat(initialStats.getStats().get(BATCH_JOB_INSTANCE).getStats().get(COMPLETED)).isEqualTo(1);
        assertThat(initialStats.getStats().get(BATCH_JOB_INSTANCE).getStats().get(FAILED)).isEqualTo(8);
        assertThat(initialStats.getStats().get(BATCH_STEP_EXECUTION).getTotalDeleted()).isEqualTo(17);
        assertThat(initialStats.getStats().get(BATCH_STEP_EXECUTION).getStats().get(COMPLETED)).isNull();
        assertThat(initialStats.getStats().get(BATCH_STEP_EXECUTION).getStats().get(FAILED)).isEqualTo(17);
    }

    @Test
    void shouldUpdateDeletedStatsWithStatusGivenAndNoStatusGiven() {
        // given
        MetadataStats initialStats = MetadataStats.init();

        // when
        initialStats.updateDeletedStats(BATCH_JOB_EXECUTION, COMPLETED, 11);
        initialStats.updateDeletedStats(BATCH_JOB_EXECUTION, COMPLETED, 12);
        initialStats.updateDeletedStats(BATCH_JOB_EXECUTION_CONTEXT, COMPLETED, 13);
        initialStats.updateDeletedStats(BATCH_JOB_INSTANCE, COMPLETED, 14);

        initialStats.updateDeletedStats(BATCH_JOB_INSTANCE, FAILED, 7);
        initialStats.updateDeletedStats(BATCH_STEP_EXECUTION, FAILED, 18);
        initialStats.updateDeletedStats(BATCH_STEP_EXECUTION, FAILED, 6);
        initialStats.updateDeletedStats(BATCH_STEP_EXECUTION, FAILED, 5);

        initialStats.updateDeletedStats(BATCH_JOB_EXECUTION, 32);
        initialStats.updateDeletedStats(BATCH_STEP_EXECUTION, 19);

        // then
        assertThat(initialStats.getTotalDeleted()).isEqualTo(137);
        assertFalse(initialStats.getStats().isEmpty());
        assertThat(initialStats.getStats().get(BATCH_JOB_EXECUTION).getTotalDeleted()).isEqualTo(55);
        assertThat(initialStats.getStats().get(BATCH_JOB_EXECUTION).getStats().get(COMPLETED)).isEqualTo(23);
        assertThat(initialStats.getStats().get(BATCH_JOB_EXECUTION).getStats().get(FAILED)).isNull();
        assertThat(initialStats.getStats().get(BATCH_JOB_INSTANCE).getTotalDeleted()).isEqualTo(21);
        assertThat(initialStats.getStats().get(BATCH_JOB_INSTANCE).getStats().get(COMPLETED)).isEqualTo(14);
        assertThat(initialStats.getStats().get(BATCH_JOB_INSTANCE).getStats().get(FAILED)).isEqualTo(7);
        assertThat(initialStats.getStats().get(BATCH_STEP_EXECUTION).getTotalDeleted()).isEqualTo(48);
        assertThat(initialStats.getStats().get(BATCH_STEP_EXECUTION).getStats().get(COMPLETED)).isNull();
        assertThat(initialStats.getStats().get(BATCH_STEP_EXECUTION).getStats().get(FAILED)).isEqualTo(29);
    }

}