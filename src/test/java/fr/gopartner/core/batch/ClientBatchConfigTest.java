package fr.gopartner.core.batch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
class ClientBatchConfigTest {

    @Autowired
    ClientBatchConfig clientBatchConfig;

    @Test
    void GIVEN_STEP_WHEN_ClientStep_THEN_Should_named_Client_Step() {
        //WHEN
        Step step = clientBatchConfig.clientStep();
        //THEN
        Assertions.assertEquals("client step", step.getName());
    }

    @Test
    void GIVEN_Job_WHEN_job_THEN_SHOULD_return_Job_named_job_producer() {
        //WHEN
        Job job = clientBatchConfig.job();
        String jobName = job.getName().replaceAll("[0-9]", "");
        //String jobName = job.getName().replaceAll("\\d", "");
        //THEN
        Assertions.assertEquals("job-info", jobName);
    }

    @Test
    void WHEN_load_THEN_Should_Return_BatchStatus_Completed() throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        BatchStatus batchStatus = clientBatchConfig.load();
        Assertions.assertEquals(BatchStatus.COMPLETED, batchStatus);
    }
}