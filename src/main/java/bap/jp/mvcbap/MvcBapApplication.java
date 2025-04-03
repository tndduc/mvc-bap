package bap.jp.mvcbap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
public class MvcBapApplication {

    public static void main(String[] args) {
	SpringApplication.run(MvcBapApplication.class, args);
    }

}
