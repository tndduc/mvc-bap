package bap.jp.mvcbap.batch.bai3;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CleanupJobScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CleanupJobScheduler.class);
    private final JobLauncher jobLauncher;
    private final Job cleanupJob;
    private final JobRepository jobRepository;

    public CleanupJobScheduler(JobLauncher jobLauncher, Job cleanupJob, JobRepository jobRepository) {
	this.jobLauncher = jobLauncher;
	this.cleanupJob = cleanupJob;
	this.jobRepository = jobRepository;
    }

    //    @Scheduled(cron = "1 0 0 1 * ?")
    @Scheduled(fixedDelay = 1 * 60 * 1000)
    public void runCleanupJob() {
	    try {
		String cutoffDate = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ISO_DATE);
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("cutoffDate", cutoffDate)
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();

		logger.info("Bắt đầu chạy batch dọn dẹp với cutoffDate: {}", cutoffDate);
		JobExecution execution = jobLauncher.run(cleanupJob, jobParameters);
		logger.info("Batch dọn dẹp hoàn tất với trạng thái: {}", execution.getStatus());

	    } catch (Exception e) {
		logger.error("Lỗi khi chạy batch dọn dẹp", e);
	    }
	}
}