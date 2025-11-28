package order.system.cqrs.commandservice.publishers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import order.system.cqrs.commandservice.events.OrderCreatedEvent;

@Service
public class OrderEventPublisher {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public OrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void publishOrderCreated(OrderCreatedEvent event) {
		kafkaTemplate.send("order-events", event.getId(), event);
		System.out.println("Evento enviado: Orden " + event.getId());
	}

}
