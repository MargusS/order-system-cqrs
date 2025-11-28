package order.system.cqrs.commandservice.config;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import order.system.cqrs.commandservice.entities.InitializationLog;
import order.system.cqrs.commandservice.events.OrderCreatedEvent;
import order.system.cqrs.commandservice.events.ProductCreatedEvent;
import order.system.cqrs.commandservice.events.dto.OrderItemEventDto;
import order.system.cqrs.commandservice.publishers.OrderEventPublisher;
import order.system.cqrs.commandservice.publishers.ProductEventPublisher;
import order.system.cqrs.commandservice.repositories.InitializationLogRepository;
import order.system.cqrs.commandservice.repositories.OrderRepository;
import order.system.cqrs.commandservice.repositories.ProductRepository;

@Component
public class SeedDataEventPublisher {

	private static final String SEED_TASK_NAME = "INITIAL_SEED_DATA_V1";
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final OrderEventPublisher orderEventPublisher;
	private final ProductEventPublisher productEventPublisher;
	private final InitializationLogRepository initializationLogRepository;

	// Dependency Injection via constructor
	public SeedDataEventPublisher(ProductRepository productRepository,
			OrderRepository orderRepository,
			OrderEventPublisher orderEventPublisher,
			ProductEventPublisher productEventPublisher,
			InitializationLogRepository initializationLogRepository) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.orderEventPublisher = orderEventPublisher;
		this.productEventPublisher = productEventPublisher;
		this.initializationLogRepository = initializationLogRepository;
	}

	/**
	 * This method listens for the ApplicationReadyEvent, which is triggered
	 * when the Spring Boot application has fully started and the context is ready.
	 *
	 * It fetches all entities (seeded via data.sql) and publishes them as events
	 * to Kafka to synchronize the Query Service.
	 */
	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void publishSeedData() {

		// 1. Check if this task has already been executed
		if (initializationLogRepository.existsById(SEED_TASK_NAME)) {
			System.out.println(">>> SKIPPING SEED DATA: Task '" + SEED_TASK_NAME + "' already executed. <<<");
			return;
		}
		System.out.println(">>> STARTING SEED DATA SYNCHRONIZATION >>>");

		// 2. Publish Products
		// We fetch all products from PostgreSQL and map them to ProductCreatedEvent
		productRepository.findAll().forEach(product -> {
			ProductCreatedEvent event = new ProductCreatedEvent(
					product.getId().toString(),
					product.getName(),
					product.getDescription(),
					new BigDecimal(product.getPrice()));

			// Publish the event to Kafka
			productEventPublisher.publishProductCreated(event);
		});

		System.out.println(">>> PRODUCTS PUBLISHED >>>");

		// 3. Publish all Orders
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
			orderEventPublisher.publishOrderCreated(event);
		});
		System.out.println(">>> ORDERS PUBLISHED >>>");

		// 4. Mark task as executed in the database
		InitializationLog log = new InitializationLog(SEED_TASK_NAME, Instant.now());
		InitializationLog savedLog = initializationLogRepository.save(log);
		System.out.println(">>> SEED DATA SYNCHRONIZATION COMPLETED >>>");
	}
}
