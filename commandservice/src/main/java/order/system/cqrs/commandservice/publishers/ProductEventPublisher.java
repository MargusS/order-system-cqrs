package order.system.cqrs.commandservice.publishers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import order.system.cqrs.commandservice.events.ProductCreatedEvent;
import order.system.cqrs.commandservice.events.ProductDeactivatedEvent;
import order.system.cqrs.commandservice.events.ProductPriceChangedEvent;

@Service
public class ProductEventPublisher {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public ProductEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void publishProductCreated(ProductCreatedEvent event) {
		kafkaTemplate.send("product-events", event.getId(), event);
		System.out.println("Evento enviado: Producto " + event.getId());
	}

	public void publishProductPriceChanged(ProductPriceChangedEvent event) {
		kafkaTemplate.send("product-price-changed-events", event.getId(), event);
		System.out.println("Event published: Product price changed - " + event.getId());
	}

	public void publishProductDeactivated(ProductDeactivatedEvent event) {
		kafkaTemplate.send("product-deactivated-events", event.getId(), event);
		System.out.println("Event published: Product deactivated - " + event.getId());
	}

}
