package bap.jp.mvcbap.batch.bai3;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class CleanupBatchConfig {

    private final DataSource dataSource;

    public CleanupBatchConfig(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    @Bean
    public Job cleanupJob(JobRepository jobRepository, Step deleteUsersStep, Step deleteProductsStep) {
	return new JobBuilder("cleanupJob", jobRepository)
		.start(deleteUsersStep)
		.next(deleteProductsStep)
		.build();
    }

    @Bean
    public Step deleteUsersStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
				JdbcBatchItemWriter<Integer> deleteUsersWriter) {
	return new StepBuilder("deleteUsersStep", jobRepository)
		.<Integer, Integer>chunk(10, transactionManager)
		.reader(deleteUsersReader(null))
		.writer(deleteUsersWriter)
		.transactionManager(transactionManager)
		.build();
    }

    @Bean
    public Step deleteProductsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
				   JdbcBatchItemWriter<Integer> deleteProductsWriter) {
	return new StepBuilder("deleteProductsStep", jobRepository)
		.<Integer, Integer>chunk(10, transactionManager)
		.reader(deleteProductsReader(null))
		.writer(deleteProductsWriter)
		.transactionManager(transactionManager)
		.build();
    }

    @Bean
    @StepScope
    public CleanupUserItemReader deleteUsersReader(@Value("#{jobParameters['cutoffDate']}") String cutoffDate) {
	return new CleanupUserItemReader(dataSource, cutoffDate);
    }

    @Bean
    @StepScope
    public CleanupProductItemReader deleteProductsReader(@Value("#{jobParameters['cutoffDate']}") String cutoffDate) {
	return new CleanupProductItemReader(dataSource, cutoffDate);
    }

    @Bean
    public JdbcBatchItemWriter<Integer> deleteUsersWriter() {
	return new JdbcBatchItemWriterBuilder<Integer>()
		.dataSource(dataSource)
		.sql("DELETE FROM users WHERE user_id = ?")
		.itemPreparedStatementSetter((userId, ps) -> ps.setInt(1, userId))
		.build();
    }

    @Bean
    public JdbcBatchItemWriter<Integer> deleteProductsWriter() {
	return new JdbcBatchItemWriterBuilder<Integer>()
		.dataSource(dataSource)
		.sql("DELETE FROM products WHERE product_id = ?")
		.itemPreparedStatementSetter((productId, ps) -> ps.setInt(1, productId))
		.build();
    }
}