package order.system.cqrs.queryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class QueryserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryserviceApplication.class, args);
	}

}
