package order.system.cqrs.queryservice.listeners;

import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import order.system.cqrs.queryservice.entities.OrderItemReadModel;
import order.system.cqrs.queryservice.entities.OrderReadModel;
import order.system.cqrs.queryservice.events.OrderCreatedEvent;
import order.system.cqrs.queryservice.repositories.OrderReadModelRepository;
import order.system.cqrs.queryservice.repositories.ProductReadModelRepository;

@Service
public class OrderEventListener {

	private final OrderReadModelRepository orderRepository;

	public OrderEventListener(ProductReadModelRepository productRepository,
			OrderReadModelRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	/**
	 * Listener for Order events.
	 */
	@KafkaListener(topics = "order-events", groupId = "query-service-group")
	public void handleOrderCreated(OrderCreatedEvent event) {
		System.out.println(">>> Received Order Event: " + event.getId());

		// Check if order already exists
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
