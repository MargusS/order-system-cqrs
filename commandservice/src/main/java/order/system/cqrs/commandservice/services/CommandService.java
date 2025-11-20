package order.system.cqrs.commandservice.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommandService {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private static final String TOPIC = "orders";

	public CommandService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void createOrder(String orderJson) {
		kafkaTemplate.send(TOPIC, orderJson);
	}
}
