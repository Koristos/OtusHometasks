package ru.fefelov.summer.market;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.fefelov.summer.market.kafka.EventKafkaProducer;

@SpringBootTest
class SummerMarketApplicationTests {

	@Test
	void contextLoads() {
		EventKafkaProducer producer = new EventKafkaProducer();

	}

}
