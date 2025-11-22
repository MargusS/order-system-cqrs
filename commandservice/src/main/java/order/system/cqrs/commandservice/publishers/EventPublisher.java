package order.system.cqrs.commandservice.publishers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import order.system.cqrs.commandservice.events.OrderCreatedEvent;
import order.system.cqrs.commandservice.events.ProductCreatedEvent;

@Service
public class EventPublisher {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public EventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void publishProductCreated(ProductCreatedEvent event) {
		kafkaTemplate.send("product-events", event.getId(), event);
		System.out.println("Evento enviado: Producto " + event.getId());
	}

	public void publishOrderCreated(OrderCreatedEvent event) {
		kafkaTemplate.send("order-events", event.getId(), event);
		System.out.println("Evento enviado: Orden " + event.getId());
	}
}
