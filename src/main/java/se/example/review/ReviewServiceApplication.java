package se.example.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"se.example.review",
		"se.example.api.core.review",
		"se.example.util.http"
})
public class ReviewServiceApplication {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ReviewServiceApplication.class);

	public static void main(String[] args) {
		var ctx = SpringApplication.run(ReviewServiceApplication.class, args);

		String mysqlUri = ctx.getEnvironment().getProperty("spring.datasource.url");
		LOG.info("Connected to MySQL: " + mysqlUri);
	}

}
