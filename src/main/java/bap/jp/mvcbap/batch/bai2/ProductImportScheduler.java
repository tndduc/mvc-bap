package bap.jp.mvcbap.batch.bai2;


import bap.jp.mvcbap.batch.bonus.BatchSchedulerControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ProductImportScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ProductImportScheduler.class);
    private static final String FILE_PATH = "data/product.csv";

    private final JobLauncher jobLauncher;
    private final Job importProductJob;
    private final BatchSchedulerControl schedulerControl;

    @Autowired
    public ProductImportScheduler(JobLauncher jobLauncher, Job importProductJob, BatchSchedulerControl schedulerControl) {
	this.jobLauncher = jobLauncher;
	this.importProductJob = importProductJob;
	this.schedulerControl = schedulerControl;
    }

    @Scheduled(fixedDelay = 1 * 60 * 1000)
    public void runProductImportJob() throws Exception {
	if (!schedulerControl.isImportJobEnabled()) {
	    logger.info("Scheduler của importProductJob đã bị tắt.");
	    return;
	}
	File file = new File(FILE_PATH);
	if (!file.exists()) {
	    logger.info("File {} không tồn tại. Bỏ qua job import.", FILE_PATH);
	    return;
	}
	logger.info("Tìm thấy file {}. Chuẩn bị chạy job import.", FILE_PATH);

	JobParameters jobParameters = new JobParametersBuilder()
		.addString("filePath", FILE_PATH)
		.addLong("timestamp", System.currentTimeMillis())
		.toJobParameters();
	JobExecution execution = jobLauncher.run(importProductJob, jobParameters);
	logger.info("Job importProductJob chạy với execution id: {} và trạng thái: {}",
		execution.getId(), execution.getStatus());
    }
}