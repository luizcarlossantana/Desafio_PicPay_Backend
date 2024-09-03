package com.SuperBank.api;

import com.SuperBank.api.model.Insumo;
import com.SuperBank.api.repository.InsumoRepository;
import com.SuperBank.api.service.ExcelItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.Future;

@Configuration
public class BatchConfig {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    public Job job(Step step, JobCompletionNotificationListener listener) {
        return new JobBuilder("job", jobRepository)
                .start(step)
                .listener(listener)
                .build();
    }

    @Bean
    public Step step(ItemReader<Insumo> reader, ItemProcessor<Insumo, Future<Insumo>> processor, ItemWriter<Future<Insumo>> writer) {
        return new StepBuilder("step", jobRepository)
                .<Insumo, Future<Insumo>>chunk(1000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Insumo> reader(@Value("#{jobParameters['file']}") String filePath) throws Exception {
        InputStream inputStream = new FileInputStream(filePath);
        return new ExcelItemReader(inputStream);
    }

    @Bean
    public ItemProcessor<Insumo, Future<Insumo>> asyncProcessor(ItemProcessor<Insumo, Insumo> itemProcessor) {
        AsyncItemProcessor<Insumo, Insumo> asyncProcessor = new AsyncItemProcessor<>();
        asyncProcessor.setDelegate(itemProcessor);
        asyncProcessor.setTaskExecutor(taskExecutor());
        return asyncProcessor;
    }

    @Bean
    public ItemProcessor<Insumo, Insumo> processor() {
        return item -> {
            // Processamento do item aqui, se necessário
            return item;
        };
    }

    @Bean
    public ItemWriter<Future<Insumo>> asyncWriter(ItemWriter<Insumo> writer) {
        AsyncItemWriter<Insumo> asyncWriter = new AsyncItemWriter<>();
        asyncWriter.setDelegate(writer);
        return asyncWriter;
    }

    @Bean
    public ItemWriter<Insumo> writer() {
        return chunk -> insumoRepository.saveAll(chunk.getItems());
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15);      // Aumente o número de threads principais
        executor.setMaxPoolSize(30);       // Aumente o número máximo de threads
        executor.setQueueCapacity(50);     // Mantenha a capacidade da fila conforme necessário
        executor.setThreadNamePrefix("Batch-Thread-");
        executor.initialize();
        return executor;
    }

}
