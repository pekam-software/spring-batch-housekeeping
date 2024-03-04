INSERT INTO BATCH_JOB_INSTANCE (JOB_INSTANCE_ID, VERSION, JOB_NAME, JOB_KEY)
     VALUES (101, 0, 'testJob', 'e17209ac5d8a48d59b356d4e04a84990'),
            (102, 0, 'testJob', 'e17209ac5d8a48d59b356d4e04a84991'),
            (103, 0, 'testJob', 'e17209ac5d8a48d59b356d4e04a84992'),
            (104, 0, 'testJob', 'e17209ac5d8a48d59b356d4e04a84993');

INSERT INTO BATCH_JOB_EXECUTION (JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID, CREATE_TIME, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
     VALUES (1, 1, 101, CURRENT_TIMESTAMP - INTERVAL '31' DAY, CURRENT_TIMESTAMP - INTERVAL '31' DAY, CURRENT_TIMESTAMP - INTERVAL '31' DAY, 'COMPLETED', 'COMPLETED', null, CURRENT_TIMESTAMP - INTERVAL '31' DAY),
            (2, 1, 102, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'COMPLETED', 'COMPLETED', null, CURRENT_TIMESTAMP),
            (3, 1, 103, CURRENT_TIMESTAMP - INTERVAL '31' DAY, CURRENT_TIMESTAMP - INTERVAL '31' DAY, CURRENT_TIMESTAMP - INTERVAL '31' DAY, 'FAILED', 'FAILED', null, CURRENT_TIMESTAMP - INTERVAL '31' DAY),
            (4, 1, 104, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FAILED', 'FAILED', null, CURRENT_TIMESTAMP);

INSERT INTO BATCH_JOB_EXECUTION_CONTEXT (JOB_EXECUTION_ID, SHORT_CONTEXT)
     VALUES (1, '{"map":""}'),
            (2, '{"map":""}'),
            (3, '{"map":""}'),
            (4, '{"map":""}');

INSERT INTO BATCH_JOB_EXECUTION_PARAMS (JOB_EXECUTION_ID, PARAMETER_NAME, PARAMETER_TYPE, PARAMETER_VALUE, IDENTIFYING)
     VALUES (1, 'testParam', 'java.lang.String', 'testValue', 'N'),
            (2, 'testParam', 'java.lang.String', 'testValue', 'N'),
            (3, 'testParam', 'java.lang.String', 'testValue', 'N'),
            (4, 'testParam', 'java.lang.String', 'testValue', 'N');

INSERT INTO BATCH_STEP_EXECUTION (STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, CREATE_TIME, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED)
     VALUES (11, 1, 'testStep', 1, CURRENT_TIMESTAMP - INTERVAL '31' DAY, CURRENT_TIMESTAMP - INTERVAL '31' DAY, CURRENT_TIMESTAMP - INTERVAL '31' DAY, 'COMPLETED', 1, 1, 0, 1, 0, 0, 0, 0, 'COMPLETED', null, CURRENT_TIMESTAMP - INTERVAL '31' DAY),
            (12, 1, 'testStep', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'COMPLETED', 1, 2, 0, 2, 0, 0, 0, 0, 'COMPLETED', null, CURRENT_TIMESTAMP),
            (13, 1, 'testStep', 3, CURRENT_TIMESTAMP - INTERVAL '31' DAY, CURRENT_TIMESTAMP - INTERVAL '31' DAY, CURRENT_TIMESTAMP - INTERVAL '31' DAY, 'FAILED', 1, 3, 0, 3, 0, 0, 0, 0, 'FAILED', null, CURRENT_TIMESTAMP - INTERVAL '31' DAY),
            (14, 1, 'testStep', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FAILED', 1, 4, 0, 4, 0, 0, 0, 0, 'FAILED', null, CURRENT_TIMESTAMP);

INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID, SHORT_CONTEXT)
     VALUES (11, '{"map":{"entry":{"string":"JdbcCursorItemReader.read.count","int":1}}}'),
            (12, '{"map":{"entry":{"string":"JdbcCursorItemReader.read.count","int":2}}}'),
            (13, '{"map":{"entry":{"string":"JdbcCursorItemReader.read.count","int":3}}}'),
            (14, '{"map":{"entry":{"string":"JdbcCursorItemReader.read.count","int":4}}}');