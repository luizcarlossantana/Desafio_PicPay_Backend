package com.SuperBank.api;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.logging.Logger;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
    private static final Logger logger = Logger.getLogger(JobCompletionNotificationListener.class.getName());

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Código a ser executado antes do job (se necessário)
    }

//    @Override
//    public void afterJob(JobExecution jobExecution) {
//        if (!jobExecution.getStatus().isUnsuccessful()) {
//            String filePath = jobExecution.getJobParameters().getString("file");
//            File file = new File(filePath);
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (!jobExecution.getStatus().isUnsuccessful()) {
            String filePath = jobExecution.getJobParameters().getString("file");
            logger.info("File path from job parameters: " + filePath);
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    logger.info("File deleted successfully: " + filePath);
                } else {
                    logger.severe("Failed to delete file: " + filePath);
                }
            } else {
                logger.warning("File does not exist: " + filePath);
            }
        } else {
            logger.warning("Job was unsuccessful. File will not be deleted.");
        }
    }
}
