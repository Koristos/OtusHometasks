package ru.fefelov.summer.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.fefelov.summer.market.kafka.EventKafkaProducer;

@SpringBootApplication
public class SummerMarketApplication {
	public static void main(String[] args) {
		SpringApplication.run(SummerMarketApplication.class, args);
	}
}
