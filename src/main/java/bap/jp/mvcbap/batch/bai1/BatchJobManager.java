package bap.jp.mvcbap.batch.bai1;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class BatchJobManager {
    private static final Logger logger = LoggerFactory.getLogger(BatchJobManager.class);
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;
    private final Job exportOrderJob;

    @Autowired
    public BatchJobManager(JobExplorer jobExplorer, JobLauncher jobLauncher, Job exportOrderJob) {
	this.jobExplorer = jobExplorer;
	this.jobLauncher = jobLauncher;
	this.exportOrderJob = exportOrderJob;
    }

    public void restartJobIfNecessary() throws Exception {
	logger.info("Kiểm tra trạng thái của job exportOrderJob...");
	List<JobInstance> jobInstances = jobExplorer.getJobInstances("exportOrderJob", 0, 1);

	if (!jobInstances.isEmpty()) {
	    JobInstance lastJobInstance = jobInstances.get(0);
	    List<JobExecution> executions = jobExplorer.getJobExecutions(lastJobInstance);

	    for (JobExecution execution : executions) {
		logger.info("Job instance {} có trạng thái: {}" + lastJobInstance.getInstanceId()+ execution.getStatus());
		if (execution.getStatus() == BatchStatus.FAILED) {
		    logger.info("Phát hiện job thất bại, đang khởi động lại...");
		    JobParameters jobParameters = new JobParametersBuilder()
			    .addString("startTime", LocalDateTime.now().minusHours(1).toString())
			    .addString("endTime", LocalDateTime.now().toString())
			    .addLong("timestamp", System.currentTimeMillis())
			    .toJobParameters();

		    JobExecution newExecution = jobLauncher.run(exportOrderJob, jobParameters);
		    logger.info("Khởi động lại job với execution id: {}"+ newExecution.getId());
		}
	    }
	} else {
	    logger.info("Chưa có job instance nào được chạy trước đó.");
	}
    }
}