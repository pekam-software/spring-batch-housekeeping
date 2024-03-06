package org.pekam.springbatch.housekeeping.metadata.scheduler;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.pekam.springbatch.housekeeping.metadata.config.CleaningSchedulerConfig;
import org.pekam.springbatch.housekeeping.metadata.config.CleaningServiceConfig;
import org.pekam.springbatch.housekeeping.metadata.service.SpringBatchMetadataTableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import javax.sql.DataSource;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        classes = {
                DatabaseCleaningScheduler.class,
                SpringBatchMetadataTableServiceImpl.class,
                CleaningServiceConfig.class,
                CleaningSchedulerConfig.class
        }
)
@EnableAutoConfiguration
class DatabaseCleaningSchedulerTest {

    @Autowired
    private DataSource dataSource;

    @SpyBean
    private DatabaseCleaningScheduler databaseCleaningScheduler;

    @Test
    public void testIfScheduledCleanupIsTriggered() {
        // given - when - then
        Awaitility.await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    verify(databaseCleaningScheduler, atLeast(1)).scheduledCleanup();
                });
    }

}