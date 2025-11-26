package order.system.cqrs.queryservice.listeners;

import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import order.system.cqrs.queryservice.entities.OrderItemReadModel;
import order.system.cqrs.queryservice.entities.OrderReadModel;
import order.system.cqrs.queryservice.entities.ProductReadModel;
import order.system.cqrs.queryservice.events.OrderCreatedEvent;
import order.system.cqrs.queryservice.events.ProductCreatedEvent;
import order.system.cqrs.queryservice.repositories.OrderReadModelRepository;
import order.system.cqrs.queryservice.repositories.ProductReadModelRepository;

@Service
public class KafkaEventListener {

	private final ProductReadModelRepository productRepository;
	private final OrderReadModelRepository orderRepository;

	public KafkaEventListener(ProductReadModelRepository productRepository,
			OrderReadModelRepository orderRepository) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}

	/**
	 * Listener for Product events.
	 * When a ProductCreatedEvent is received, we save it to MongoDB.
	 */
	@KafkaListener(topics = "product-events", groupId = "query-service-group")
	public void handleProductCreated(ProductCreatedEvent event) {
		System.out.println(">>> Received Product Event: " + event.getId());

		//Check if product already exists
		ProductReadModel product = productRepository.findByProductId(event.getId())
				.orElse(new ProductReadModel());

		// We use the ID coming from the event (the UUID string) as our business key
		product.setProductId(event.getId());
		product.setName(event.getName());
		product.setDescription(event.getDescription());
		product.setPrice(event.getPrice());

		productRepository.save(product);
		System.out.println(">>> Product saved to MongoDB: " + product.getName());
	}

	/**
	 * Listener for Order events.
	 */
	@KafkaListener(topics = "order-events", groupId = "query-service-group")
	public void handleOrderCreated(OrderCreatedEvent event) {
		System.out.println(">>> Received Order Event: " + event.getId());

		//Check if order already exists
		OrderReadModel order = orderRepository.findByOrderId(event.getId())
				.orElse(new OrderReadModel());

		order.setOrderId(event.getId());
		order.setStatus(event.getStatus());
		order.setCreatedAt(event.getCreatedAt());

		if (event.getItems() != null) {
			order.setItems(event.getItems().stream().map(itemDto -> {
				OrderItemReadModel item = new OrderItemReadModel();
				item.setProductId(itemDto.getProductId());
				item.setQuantity(itemDto.getQuantity());
				item.setPrice(itemDto.getPrice());
				return item;
			}).collect(Collectors.toList()));
		}

		orderRepository.save(order);
		System.out.println(">>> Order saved to MongoDB: " + order.getOrderId());
	}
}
