package org.pekam.springbatch.housekeeping.metadata.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.pekam.springbatch.housekeeping.metadata.constants.ExecutionStatus.FAILED;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_JOB_EXECUTION_CONTEXT;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_JOB_INSTANCE;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_STEP_EXECUTION;
import static org.pekam.springbatch.housekeeping.metadata.constants.Table.BATCH_STEP_EXECUTION_CONTEXT;

class DeleteQueryFactoryTest {

    @Test
    void shouldSanitizeEmptyPrefix() {
        // given
        final String expectedQuery = """
                DELETE
                  FROM BATCH_JOB_INSTANCE
                 WHERE JOB_INSTANCE_ID NOT IN (
                   SELECT BATCH_JOB_EXECUTION.JOB_INSTANCE_ID
                     FROM BATCH_JOB_EXECUTION
                 )
                """;

        // when
        final var deleteQuery = DeleteQueryFactory.get("", BATCH_JOB_INSTANCE, FAILED);

        // then
        assertThat(deleteQuery).isEqualTo(expectedQuery);
    }

    @Test
    void shouldSanitizeBlankPrefix() {
        // given
        final String expectedQuery = """
                DELETE
                  FROM BATCH_STEP_EXECUTION
                 WHERE JOB_EXECUTION_ID IN (
                   SELECT JOB_EXECUTION_ID
                     FROM BATCH_JOB_EXECUTION
                    WHERE STATUS = 'FAILED' AND CREATE_TIME <= ?
                 )
                """;

        // when
        final var deleteQuery = DeleteQueryFactory.get("    ", BATCH_STEP_EXECUTION, FAILED);

        // then
        assertThat(deleteQuery).isEqualTo(expectedQuery);
    }

    @Test
    void shouldSanitizeNullPrefix() {
        // given
        final String expectedQuery = """
                DELETE
                  FROM BATCH_STEP_EXECUTION_CONTEXT
                 WHERE STEP_EXECUTION_ID IN (
                   SELECT BATCH_STEP_EXECUTION.STEP_EXECUTION_ID
                     FROM BATCH_STEP_EXECUTION
                    WHERE JOB_EXECUTION_ID IN (
                      SELECT JOB_EXECUTION_ID
                        FROM BATCH_JOB_EXECUTION
                       WHERE STATUS = 'FAILED' AND CREATE_TIME <= ?
                    )
                 )
                """;

        // when
        final var deleteQuery = DeleteQueryFactory.get(null, BATCH_STEP_EXECUTION_CONTEXT, FAILED);

        // then
        assertThat(deleteQuery).isEqualTo(expectedQuery);
    }

    @Test
    void shouldGetQueryWithCreateDatePart() {
        // given
        final String expectedQuery = """
                DELETE
                  FROM PREFIX_JOB_EXECUTION_CONTEXT
                 WHERE JOB_EXECUTION_ID IN (
                   SELECT JOB_EXECUTION_ID
                     FROM PREFIX_JOB_EXECUTION
                    WHERE STATUS = 'FAILED' AND CREATE_TIME <= ?
                 )
                """;

        // when
        final var deleteQuery = DeleteQueryFactory.get("PREFIX_", BATCH_JOB_EXECUTION_CONTEXT, FAILED);

        // then
        assertThat(deleteQuery).isEqualTo(expectedQuery);
    }

    @Test
    void shouldGetQueryWithWithoutCreateDatePart() {
        final String expectedQuery = """
                DELETE
                  FROM PREFIX_JOB_INSTANCE
                 WHERE JOB_INSTANCE_ID NOT IN (
                   SELECT PREFIX_JOB_EXECUTION.JOB_INSTANCE_ID
                     FROM PREFIX_JOB_EXECUTION
                 )
                """;

        // when
        final var deleteQuery = DeleteQueryFactory.get("PREFIX_", BATCH_JOB_INSTANCE);

        // then
        assertThat(deleteQuery).isEqualTo(expectedQuery);
    }

}