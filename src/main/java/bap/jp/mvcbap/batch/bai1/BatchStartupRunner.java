package bap.jp.mvcbap.batch.bai1;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class BatchStartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BatchStartupRunner.class);
    private final BatchJobManager batchJobManager;

    public BatchStartupRunner(BatchJobManager batchJobManager) {
	this.batchJobManager = batchJobManager;
    }

    @Override
    public void run(String... args) throws Exception {
	logger.info("Ứng dụng khởi động. Đang kiểm tra và chạy lại job nếu cần...");
	batchJobManager.restartJobIfNecessary();
	logger.info("Quá trình kiểm tra job hoàn tất.");
    }
}