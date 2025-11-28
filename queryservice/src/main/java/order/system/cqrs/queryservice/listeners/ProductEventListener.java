package order.system.cqrs.queryservice.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import order.system.cqrs.queryservice.entities.ProductReadModel;
import order.system.cqrs.queryservice.events.ProductCreatedEvent;
import order.system.cqrs.queryservice.events.ProductDeactivatedEvent;
import order.system.cqrs.queryservice.events.ProductPriceChangedEvent;
import order.system.cqrs.queryservice.repositories.OrderReadModelRepository;
import order.system.cqrs.queryservice.repositories.ProductReadModelRepository;

@Service
public class ProductEventListener {

	private final ProductReadModelRepository productRepository;

	public ProductEventListener(ProductReadModelRepository productRepository,
			OrderReadModelRepository orderRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Listener for Product events.
	 * When a ProductCreatedEvent is received, we save it to MongoDB.
	 */
	@KafkaListener(topics = "product-events", groupId = "query-service-group")
	public void handleProductCreated(ProductCreatedEvent event) {
		System.out.println(">>> Received Product Event: " + event.getId());

		// Check if product already exists
		ProductReadModel product = productRepository.findByProductId(event.getId())
				.orElse(new ProductReadModel());

		// We use the ID coming from the event (the UUID string) as our business key
		product.setProductId(event.getId());
		product.setName(event.getName());
		product.setDescription(event.getDescription());
		product.setPrice(event.getPrice());
		product.setIsActive(event.getIsActive());

		productRepository.save(product);
		System.out.println(">>> Product saved to MongoDB: " + product.getName());
	}

	/**
	 * Handle price changes.
	 * Updates only the price field of the existing document.
	 */
	@KafkaListener(topics = "product-price-changed-events", groupId = "query-service-group")
	public void handleProductPriceChanged(ProductPriceChangedEvent event) {
		System.out.println(">>> Received Price Change Event for Product: " + event.getId());

		productRepository.findByProductId(event.getId()).ifPresentOrElse(
				product -> {
					product.setPrice(event.getNewPrice());
					productRepository.save(product);
					System.out.println(">>> Updated Product Price in MongoDB");
				},
				() -> System.out.println(">>> ERROR: Product not found for price update: " + event.getId()));
	}

	/**
	 * Handle product deactivation (Soft Delete).
	 * Sets active = false.
	 */
	@KafkaListener(topics = "product-deactivated-events", groupId = "query-service-group")
	public void handleProductDeactivated(ProductDeactivatedEvent event) {
		System.out.println(">>> Received Deactivation Event for Product: " + event.getId());

		productRepository.findByProductId(event.getId()).ifPresentOrElse(
				product -> {
					product.setIsActive(false);
					productRepository.save(product);
					System.out.println(">>> Deactivated Product in MongoDB");
				},
				() -> System.out.println(">>> ERROR: Product not found for deactivation: " + event.getId()));
	}

}
