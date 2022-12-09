package fr.gopartner.core.batch;

import fr.gopartner.domain.client.Client;
import fr.gopartner.domain.client.ClientDto;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@EnableBatchProcessing
@Configuration
public class ClientBatchConfig {

    @Value("${inputFile}")
    private String url;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ClientProcessor clientProcessor;
    private final ClientItemWriter clientItemWriter;
    private final JobLauncher jobLauncher;

    public ClientBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, JobLauncher jobLauncher,
                             ClientProcessor clientProcessor, ClientItemWriter clientItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobLauncher = jobLauncher;
        this.clientProcessor = clientProcessor;
        this.clientItemWriter = clientItemWriter;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job-info" + java.lang.System.currentTimeMillis())
                .incrementer(new RunIdIncrementer())
                .start(clientStep())
                .build();
    }

    @Bean
    public Step clientStep() {
        return stepBuilderFactory.get("client step")
                .<ClientDto, Client>chunk(10)
                .reader(salesInfoFileReader())
                .processor(clientProcessor)
                .writer(clientItemWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<ClientDto> salesInfoFileReader() {
        return new FlatFileItemReaderBuilder<ClientDto>()
                .resource(new ClassPathResource(url))
                .name("clientFileReader")
                .delimited()
                .delimiter(",")
                .names(new String[]{"name", "address", "email"})
                .linesToSkip(1)
                .targetType(ClientDto.class)
                .build();
    }

    @Bean
    public BatchStatus load() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        var parameters = new JobParameters();
        var jobExecution = jobLauncher.run(job(), parameters);
        return jobExecution.getStatus();
    }

}
