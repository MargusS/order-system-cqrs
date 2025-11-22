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

		ProductReadModel product = new ProductReadModel();
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

		OrderReadModel order = new OrderReadModel();
		order.setOrderId(event.getId());
		order.setStatus(event.getStatus());
		order.setCreatedAt(event.getCreatedAt());

		// Map items. Notice we might miss product names here if we don't look them up,
		// but for now we map what comes in the event.
		// Ideally, the event should carry enough info, or we fetch product details from
		// our local Mongo DB.
		// For this simple step, let's just map the IDs and quantity.
		if (event.getItems() != null) {
			order.setItems(event.getItems().stream().map(itemDto -> {
				OrderItemReadModel item = new OrderItemReadModel();
				item.setProductId(itemDto.getProductId());
				item.setQuantity(itemDto.getQuantity());
				item.setPrice(itemDto.getPrice());
				// item.setProductName(...) -> In a real app, you might look this up in
				// productRepository
				// using the productId to enrich the read model.
				return item;
			}).collect(Collectors.toList()));
		}

		orderRepository.save(order);
		System.out.println(">>> Order saved to MongoDB: " + order.getOrderId());
	}
}
