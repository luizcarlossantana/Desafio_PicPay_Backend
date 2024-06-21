package com.SuperBank.api;

import com.SuperBank.api.model.Insumo;
import com.SuperBank.api.repository.InsumoRepository;
import com.SuperBank.api.service.ExcelItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Configuration
public class BatchConfig {

    @Autowired
private PlatformTransactionManager transactionManager;

   @Autowired
    InsumoRepository insumoRepository;


@Bean
public Job job(Step step ,JobRepository jobRepository){
    return new JobBuilder("job",jobRepository)
            .start(step)
            .build();
}
    @Bean
    public Step step(JobRepository jobRepository,ItemReader<Insumo> reader, ItemWriter<Insumo> writer) {
        return new StepBuilder("step", jobRepository)
                .<Insumo, Insumo>chunk(200,transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Insumo> reader(@Value("#{jobParameters['file']}") MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        return new ExcelItemReader(inputStream);
    }

    @Bean
    public ItemWriter<Insumo> writer() {
        return new ItemWriter<Insumo>() {
            @Override
            public void write(Chunk<? extends Insumo> chunk) throws Exception {
                insumoRepository.saveAll(chunk.getItems());
            }
        };
    }
}
