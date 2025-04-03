package bap.jp.mvcbap.batch.bai1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final DataSource dataSource;

    public BatchConfig(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    @Bean
    public Job exportOrderJob(JobRepository jobRepository, Step exportStep) {
	return new JobBuilder("exportOrderJob", jobRepository)
		.incrementer(new RunIdIncrementer())
		.start(exportStep)
		.build();
    }

    @Bean
    public Step exportStep(JobRepository jobRepository,
			   PlatformTransactionManager transactionManager,
			   JdbcCursorItemReader<OrderExport> itemReader,
			   FlatFileItemWriter<OrderExport> itemWriter) {
	return new StepBuilder("exportStep", jobRepository)
		.<OrderExport, OrderExport>chunk(3, transactionManager)
		.reader(itemReader)
		.writer(itemWriter)
		.build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<OrderExport> itemReader(
	    @Value("#{jobParameters['startTime']}") String startTime,
	    @Value("#{jobParameters['endTime']}") String endTime) {

	JdbcCursorItemReader<OrderExport> reader = new JdbcCursorItemReader<>();
	reader.setDataSource(dataSource);
	reader.setSql("SELECT o.order_id, o.order_date, o.total_amount, o.user_id, u.user_name " +
		"FROM orders o JOIN users u ON o.user_id = u.user_id " +
		"WHERE o.order_date >= ? AND o.order_date < ? " +
		"AND o.delete_flg = false AND u.delete_flg = false");

	reader.setRowMapper((rs, rowNum) -> {
	    OrderExport orderExport = new OrderExport();
	    orderExport.setOrderId(rs.getInt("order_id"));
	    orderExport.setOrderDate(rs.getTimestamp("order_date").toInstant());
	    orderExport.setTotalAmount(rs.getBigDecimal("total_amount"));
	    orderExport.setUserId(rs.getInt("user_id"));
	    orderExport.setUserName(rs.getString("user_name"));
	    org.slf4j.LoggerFactory.getLogger("ItemReader")
		    .info("Đọc đơn hàng: {}", orderExport);
	    return orderExport;
	});

	reader.setPreparedStatementSetter(ps -> {
	    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.parse(startTime)));
	    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.parse(endTime)));
	});
	reader.setSaveState(false);
	reader.setVerifyCursorPosition(false);
	return reader;
    }

    @Bean
    public FlatFileItemWriter<OrderExport> itemWriter() {
	String filename = "orders_export.csv";
	return new FlatFileItemWriterBuilder<OrderExport>()
		.name("orderItemWriter")
		.resource(new FileSystemResource(filename))
		.headerCallback(writer -> writer.write("order_id,order_date,total_amount,user_id,user_name"))
		.lineAggregator(new DelimitedLineAggregator<OrderExport>() {{
		    setDelimiter(",");
		    setFieldExtractor(new BeanWrapperFieldExtractor<OrderExport>() {{
			setNames(new String[]{"orderId", "orderDate", "totalAmount", "userId", "userName"});
		    }});
		}})
		.build();
    }
}
