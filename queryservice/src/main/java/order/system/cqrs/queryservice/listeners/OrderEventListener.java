package order.system.cqrs.queryservice.listeners;

import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import order.system.cqrs.queryservice.entities.OrderItemReadModel;
import order.system.cqrs.queryservice.entities.OrderReadModel;
import order.system.cqrs.queryservice.events.OrderCancelledEvent;
import order.system.cqrs.queryservice.events.OrderConfirmedEvent;
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

	/**
	 * Listener for Order Confirmed events.
	 * Updates the order status to 'CONFIRMED'.
	 */
	@KafkaListener(topics = "order-confirmed-events", groupId = "query-service-group")
	public void handleOrderConfirmed(OrderConfirmedEvent event) {
		System.out.println(">>> Received Order Confirmed Event: " + event.getId());

		orderRepository.findByOrderId(event.getId()).ifPresentOrElse(
				order -> {
					order.setStatus("CONFIRMED");
					orderRepository.save(order);
					System.out.println(">>> Updated Order Status to CONFIRMED in MongoDB");
				},
				() -> System.out.println(">>> ERROR: Order not found for confirmation: " + event.getId()));
	}

	/**
	 * Listener for Order Cancelled events.
	 * Updates the order status to 'CANCELLED'.
	 */
	@KafkaListener(topics = "order-cancelled-events", groupId = "query-service-group")
	public void handleOrderCancelled(OrderCancelledEvent event) {
		System.out.println(">>> Received Order Cancelled Event: " + event.getId());

		orderRepository.findByOrderId(event.getId()).ifPresentOrElse(
				order -> {
					order.setStatus("CANCELLED");
					// order.setCancelReason(event.getReason());
					orderRepository.save(order);
					System.out.println(">>> Updated Order Status to CANCELLED in MongoDB");
				},
				() -> System.out.println(">>> ERROR: Order not found for cancellation: " + event.getId()));
	}

}
