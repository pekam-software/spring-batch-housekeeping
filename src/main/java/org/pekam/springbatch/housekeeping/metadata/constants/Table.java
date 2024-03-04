package org.pekam.springbatch.housekeeping.metadata.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Table {
    BATCH_STEP_EXECUTION_CONTEXT(
            """
                    DELETE
                      FROM %1$sSTEP_EXECUTION_CONTEXT
                     WHERE STEP_EXECUTION_ID IN (
                       SELECT %1$sSTEP_EXECUTION.STEP_EXECUTION_ID
                         FROM %1$sSTEP_EXECUTION
                        WHERE JOB_EXECUTION_ID IN (
                          SELECT JOB_EXECUTION_ID
                            FROM %1$sJOB_EXECUTION
                           WHERE STATUS = '%2$s' %3$s
                        )
                     )
                    """
    ),
    BATCH_STEP_EXECUTION(
            """
                    DELETE
                      FROM %1$sSTEP_EXECUTION
                     WHERE JOB_EXECUTION_ID IN (
                       SELECT JOB_EXECUTION_ID
                         FROM %1$sJOB_EXECUTION
                        WHERE STATUS = '%2$s' %3$s
                     )
                    """
    ),
    BATCH_JOB_EXECUTION_CONTEXT(
            """
                    DELETE
                      FROM %1$sJOB_EXECUTION_CONTEXT
                     WHERE JOB_EXECUTION_ID IN (
                       SELECT JOB_EXECUTION_ID
                         FROM %1$sJOB_EXECUTION
                        WHERE STATUS = '%2$s' %3$s
                     )
                    """
    ),
    BATCH_JOB_EXECUTION_PARAMS(
            """
                    DELETE
                      FROM %1$sJOB_EXECUTION_PARAMS
                     WHERE JOB_EXECUTION_ID IN (
                       SELECT JOB_EXECUTION_ID
                         FROM %1$sJOB_EXECUTION
                        WHERE STATUS = '%2$s' %3$s
                     )
                    """
    ),
    BATCH_JOB_EXECUTION(
            """
                    DELETE
                      FROM %1$sJOB_EXECUTION
                     WHERE STATUS = '%2$s' %3$s
                    """
    ),
    BATCH_JOB_INSTANCE(
                    """
                    DELETE
                      FROM %1$sJOB_INSTANCE
                     WHERE JOB_INSTANCE_ID NOT IN (
                       SELECT %1$sJOB_EXECUTION.JOB_INSTANCE_ID
                         FROM %1$sJOB_EXECUTION
                     )
                    """
    );

    private final String deleteQuery;

}
