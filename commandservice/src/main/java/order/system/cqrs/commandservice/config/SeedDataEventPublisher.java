package order.system.cqrs.commandservice.config;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import order.system.cqrs.commandservice.events.OrderCreatedEvent;
import order.system.cqrs.commandservice.events.ProductCreatedEvent;
import order.system.cqrs.commandservice.events.dto.OrderItemEventDto;
import order.system.cqrs.commandservice.publishers.EventPublisher;
import order.system.cqrs.commandservice.repositories.OrderRepository;
import order.system.cqrs.commandservice.repositories.ProductRepository;

@Component
public class SeedDataEventPublisher {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final EventPublisher eventPublisher;

	// Dependency Injection via constructor
	public SeedDataEventPublisher(ProductRepository productRepository,
			OrderRepository orderRepository,
			EventPublisher eventPublisher) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.eventPublisher = eventPublisher;
	}

	/**
	 * This method listens for the ApplicationReadyEvent, which is triggered
	 * when the Spring Boot application has fully started and the context is ready.
	 *
	 * It fetches all entities (seeded via data.sql) and publishes them as events
	 * to Kafka to synchronize the Query Service.
	 */
	@EventListener(ApplicationReadyEvent.class)
	@Transactional(readOnly = true) // Open a transaction to safely read lazy-loaded relationships
	public void publishSeedData() {
		System.out.println(">>> STARTING SEED DATA SYNCHRONIZATION >>>");

		// 1. Publish all Products
		// We fetch all products from PostgreSQL and map them to ProductCreatedEvent
		productRepository.findAll().forEach(product -> {
			ProductCreatedEvent event = new ProductCreatedEvent(
					product.getId().toString(),
					product.getName(),
					product.getDescription(),
					new BigDecimal(product.getPrice()));

			// Publish the event to Kafka
			eventPublisher.publishProductCreated(event);
		});

		System.out.println(">>> PRODUCTS PUBLISHED >>>");

		// 2. Publish all Orders
		// We fetch all orders and map them to OrderCreatedEvent
		orderRepository.findAll().forEach(order -> {

			// Map the list of OrderItem entities to OrderItemEventDto DTOs
			List<OrderItemEventDto> itemDtoList = order.getItems().stream()
					.map(item -> new OrderItemEventDto(
							item.getProduct().getId().toString(),
							item.getQuantity(),
							new BigDecimal(item.getProduct().getPrice())))
					.collect(Collectors.toList());

			OrderCreatedEvent event = new OrderCreatedEvent(
					order.getId().toString(),
					order.getStatus(),
					order.getCreatedAt(),
					itemDtoList);

			// Publish the event to Kafka
			eventPublisher.publishOrderCreated(event);
		});

		System.out.println(">>> ORDERS PUBLISHED >>>");
		System.out.println(">>> SEED DATA SYNCHRONIZATION COMPLETED >>>");
	}
}
