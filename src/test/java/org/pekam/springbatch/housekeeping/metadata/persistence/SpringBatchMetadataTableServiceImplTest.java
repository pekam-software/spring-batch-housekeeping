package org.pekam.springbatch.housekeeping.metadata.persistence;

import org.junit.jupiter.api.Test;
import org.pekam.springbatch.housekeeping.metadata.config.CleaningServiceConfig;
import org.pekam.springbatch.housekeeping.metadata.service.SpringBatchMetadataTableService;
import org.pekam.springbatch.housekeeping.metadata.service.SpringBatchMetadataTableServiceImpl;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = { SpringBatchMetadataTableServiceImpl.class, CleaningServiceConfig.class }
)
@EnableAutoConfiguration
class SpringBatchMetadataTableServiceImplTest {

    @Autowired
    private CleaningServiceConfig cleaningServiceConfig;

    @Autowired
    private SpringBatchMetadataTableService springBatchMetadataTableService;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private DataSource dataSource;

    @Test
    void shouldCleanMetadataTables() throws SQLException {
        // given
        Resource sqlScript = new ClassPathResource("db/data/insert_test_spring_batch_data.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), sqlScript);

        final var initialJobInstances = jobExplorer.getJobInstances("testJob", 0, 5);

        // when
        springBatchMetadataTableService.cleanMetadataTables(cleaningServiceConfig);
        final var jobsAfterCleanup = jobExplorer.getJobInstances("testJob", 0, 5);

        // then
        assertThat(initialJobInstances.size()).isEqualTo(4);
        assertThat(
                initialJobInstances.stream()
                        .map(JobInstance::getInstanceId)
                        .toList()
        ).hasSameElementsAs(
                List.of(101L, 102L, 103L, 104L)
        );
        assertThat(jobsAfterCleanup.size()).isEqualTo(1);
        assertThat(jobsAfterCleanup.getFirst().getInstanceId()).isEqualTo(104);
    }

}