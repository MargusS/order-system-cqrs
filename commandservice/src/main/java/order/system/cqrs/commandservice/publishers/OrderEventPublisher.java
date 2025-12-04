package order.system.cqrs.commandservice.publishers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import order.system.cqrs.commandservice.events.OrderCancelledEvent;
import order.system.cqrs.commandservice.events.OrderConfirmedEvent;
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

	public void publishOrderConfirmed(OrderConfirmedEvent event) {
		kafkaTemplate.send("order-confirmed-events", event.getId(), event);
		System.out.println("Event published: Order Confirmed - " + event.getId());
	}

	public void publishOrderCancelled(OrderCancelledEvent event) {
		kafkaTemplate.send("order-cancelled-events", event.getId(), event);
		System.out.println("Event published: Order Cancelled - " + event.getId());
	}

}
