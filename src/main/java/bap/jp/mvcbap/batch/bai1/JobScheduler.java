package bap.jp.mvcbap.batch.bai1;

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

import java.time.LocalDateTime;

@Component
public class JobScheduler {

    private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);
    private final JobLauncher jobLauncher;
    private final Job exportOrderJob;

    @Autowired
    public JobScheduler(JobLauncher jobLauncher, Job exportOrderJob) {
	this.jobLauncher = jobLauncher;
	this.exportOrderJob = exportOrderJob;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void runJob() throws Exception {
	logger.info("Scheduler kích hoạt. Chuẩn bị chạy job exportOrderJob...");
	LocalDateTime now = LocalDateTime.now();
	LocalDateTime startTime = now.minusHours(1);

	JobParameters jobParameters = new JobParametersBuilder()
		.addString("startTime", startTime.toString())
		.addString("endTime", now.toString())
		.addLong("timestamp", System.currentTimeMillis())
		.toJobParameters();

	JobExecution execution = jobLauncher.run(exportOrderJob, jobParameters);
	logger.info("Job exportOrderJob đã được chạy với execution id: {} và trạng thái: {}",
		execution.getId(), execution.getStatus());
    }
}