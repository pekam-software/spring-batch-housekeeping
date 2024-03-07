# spring-batch-housekeeping

[![CI](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/gradle.yml/badge.svg)](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/gradle.yml)
[![CodeQL](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/codeql.yml/badge.svg)](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/codeql.yml)
[![Publish](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/publish.yml/badge.svg)](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/publish.yml)

A small library containing useful code for Spring Batch housekeeping making its performance better.

## Spring Batch Metadata Tables Housekeeping

### Summary

This part of the library contains a code that can be used for the Spring Batch metadata tables cleaning. Spring Batch is 
not doing this by default so the tables are constantly growing in size having eventually a significant impact for the 
performance of the app or microservice. In order to deal with this situation this library is providing a configurable 
service that can be used to clean the metadata tables as well as the annotation that enables a scheduler that will be 
cleaning the mentioned tables according to the configurable CRON expression. The scheduler is safe to be used in 
multi-instance environment as it is using a shared lock mechanism to prevent being run by the multiple instances of the 
app or microservice.

### Prerequisites

To make the cleaning functionality run smoothly and without problems the following prerequisites have to be fulfilled:

- Spring Batch has to be in the version 5,
- if you would like to use a scheduler you have to add to your app or microservice a db table to create a shedlock 
  (shared lock) table (example in postgreSQL):
  ```sql
  CREATE TABLE IF NOT EXISTS shedlock (
    name       VARCHAR(64),
    lock_until TIMESTAMP(3) NULL,
    locked_at  TIMESTAMP(3) NULL,
    locked_by  VARCHAR(255),
    PRIMARY KEY(name)
  );
  ```
- the following properties have to be available in your application configuration with the proper values set:
  ```yaml
  spring:
    batch:
      housekeeping:
        metadata-tables:
          cleaning-enabled-for-completed: true
          cleaning-enabled-for-failed: true
          retention-days-for-completed: 0
          retention-days-for-failed: 30
          prefix: ""
          scheduler:
            cron: "* * */12 ? * *"
            shedlock-table:
              prefix: ""
  ```
  - **cleaning-enabled-for-completed** is a flag that enables cleaning the rows with COMPLETED status,
  - **cleaning-enabled-for-failed** is a flag that enables cleaning the rows with FAILED status,
  - **retention-days-for-completed** the rows with the COMPLETED status will be removed if they are created more than
    this value days ago,
  - **retention-days-for-failed** the rows with the FAILED status will be removed if they are created more than this 
    value days ago,
  - **prefix** it is a prefix for Spring Batch metadata tables, if not given the default prefix *BATCH_* will be used
  - **scheduler.cron** it is a schedule that will be followed when cleaning the tables i.e. every 12 hours,
  - **scheduler.shedlock-table.prefix** it is a prefix for a shedlock table, usually your db schema name.

It is also recommended for the performance gain to create indexes for Spring Batch metadata tables. It should make the 
processing much faster than without the indexes (example in postgreSQL):

```sql
CREATE INDEX IF NOT EXISTS idx_batch_job_instance_job_name_job_key
    ON BATCH_JOB_INSTANCE(JOB_NAME, JOB_KEY);

CREATE INDEX IF NOT EXISTS idx_batch_job_execution_job_instance_id
    ON BATCH_JOB_EXECUTION(JOB_INSTANCE_ID);

CREATE INDEX IF NOT EXISTS idx_batch_step_execution_version
    ON BATCH_STEP_EXECUTION(VERSION);
    
CREATE INDEX IF NOT EXISTS idx_batch_step_execution_step_name_job_execution_id
    ON BATCH_STEP_EXECUTION(STEP_NAME, JOB_EXECUTION_ID);

CREATE INDEX IF NOT EXISTS idx_batch_execution_params_job_execution_id
    ON BATCH_EXECUTION_PARAMS(JOB_EXECUTION_ID);

CREATE INDEX IF NOT EXISTS idx_batch_job_execution_status
    ON BATCH_JOB_EXECUTION(STATUS);    
```

###Usage
To enable housekeeping scheduler please use proper annotation on your application class i.e.
```java
@EnableSpringBatchHousekeeping
public class YourApp {
    public static void main(String[] args) {
        SpringApplication.run(YourApp.class, args);
    }
}
```

You can as well use interface to trigger cleaning manually:
```java
public interface SpringBatchMetadataTableService {

    MetadataStats cleanMetadataTables(CleaningServiceConfig cleaningServiceConfig);

}
```