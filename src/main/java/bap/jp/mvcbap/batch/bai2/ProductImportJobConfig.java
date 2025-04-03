package bap.jp.mvcbap.batch.bai2;


import bap.jp.mvcbap.entity.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class ProductImportJobConfig {

    private final DataSource dataSource;

    public ProductImportJobConfig(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    @Bean
    public Job importProductJob(JobRepository jobRepository, Step importProductStep) {
	return new JobBuilder("importProductJob", jobRepository)
		.incrementer(new RunIdIncrementer())
		.start(importProductStep)
		.build();
    }

    @Bean
    public Step importProductStep(JobRepository jobRepository,
				  PlatformTransactionManager transactionManager,
				  FlatFileItemReader<Product> productItemReader,
				  JdbcBatchItemWriter<Product> productItemWriter,
				  SkipListener<Product, Product> productSkipListener) {
	return new StepBuilder("importProductStep", jobRepository)
		.<Product, Product>chunk(10, transactionManager)
		.reader(productItemReader)
		.writer(productItemWriter)
		.faultTolerant()
		.skip(Exception.class)
		.skipLimit(Integer.MAX_VALUE)
		.listener(productSkipListener)
		.build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Product> productItemReader(@Value("#{jobParameters['filePath']}") String filePath) {
	FlatFileItemReader<Product> reader = new FlatFileItemReader<>();
	reader.setResource(new FileSystemResource(filePath));
	reader.setLinesToSkip(1);
	CustomLineMapper<Product> lineMapper = new CustomLineMapper<>();
	DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
	tokenizer.setNames("productName", "price", "categoryId", "deleteFlg");
	lineMapper.setLineTokenizer(tokenizer);
	lineMapper.setFieldSetMapper(new ProductFieldSetMapper());
	reader.setLineMapper(lineMapper);
	return reader;
    }

    @Bean
    public JdbcBatchItemWriter<Product> productItemWriter() {
	return new JdbcBatchItemWriterBuilder<Product>()
		.dataSource(dataSource)
		.sql("INSERT INTO products (product_name, price, category_id, delete_flg) " +
			"VALUES (:productName, :price, :category.id, :deleteFlg) " +
			"ON DUPLICATE KEY UPDATE product_name=:productName, price=:price, category_id=:category.id, delete_flg=:deleteFlg")
		.beanMapped()
		.build();
    }

    @Bean
    public SkipListener<Product, Product> productSkipListener() {
	return new SkipListener<Product, Product>() {
	    @Override
	    public void onSkipInRead(Throwable t) {
		// Đã được log trong CustomLineMapper
	    }

	    @Override
	    public void onSkipInWrite(Product item, Throwable t) {
		System.err.println("Error in writing product " + item + ": " + t.getMessage());
	    }

	    @Override
	    public void onSkipInProcess(Product item, Throwable t) {
		System.err.println("Error in processing product " + item + ": " + t.getMessage());
	    }
	};
    }
}