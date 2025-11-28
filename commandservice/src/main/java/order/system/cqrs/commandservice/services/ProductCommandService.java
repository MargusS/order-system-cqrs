package order.system.cqrs.commandservice.services;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import order.system.cqrs.commandservice.controllers.request.CreateProductRequest;
import order.system.cqrs.commandservice.controllers.request.UpdateProductPriceRequest;
import order.system.cqrs.commandservice.controllers.response.ProductResponse;
import order.system.cqrs.commandservice.entities.Product;
import order.system.cqrs.commandservice.events.ProductCreatedEvent;
import order.system.cqrs.commandservice.events.ProductDeactivatedEvent;
import order.system.cqrs.commandservice.events.ProductPriceChangedEvent;
import order.system.cqrs.commandservice.publishers.ProductEventPublisher;
import order.system.cqrs.commandservice.repositories.ProductRepository;

@Service
public class ProductCommandService {

	private final ProductRepository productRepository;
	private final ProductEventPublisher eventPublisher;

	public ProductCommandService(ProductRepository productRepository,
			ProductEventPublisher eventPublisher) {
		this.productRepository = productRepository;
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Create a new product and publish a ProductCreatedEvent.
	 */
	@Transactional
	public ProductResponse createProduct(CreateProductRequest request) {
		// 1. Validate input
		if (request.getPrice() <= 0) {
			throw new IllegalArgumentException("Price must be greater than 0");
		}

		// 2. Create entity
		Product product = new Product();
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setActive(true);

		// 3. Persist to database
		Product savedProduct = productRepository.save(product);

		// 4. Publish event (for Query Service synchronization)
		ProductCreatedEvent event = new ProductCreatedEvent(
				savedProduct.getId().toString(),
				savedProduct.getName(),
				savedProduct.getDescription(),
				BigDecimal.valueOf(savedProduct.getPrice()),
				savedProduct.getActive());
		eventPublisher.publishProductCreated(event);

		// 5. Return DTO
		return mapToResponse(savedProduct);
	}

	/**
	 * Update product price and publish a ProductPriceChangedEvent.
	 */
	@Transactional
	public ProductResponse updateProductPrice(UUID productId, UpdateProductPriceRequest request) {
		// 1. Validate input
		if (request.getNewPrice() <= 0) {
			throw new IllegalArgumentException("New price must be greater than 0");
		}

		// 2. Find product or throw exception
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

		// 3. Check if product is active
		if (!product.getActive()) {
			throw new RuntimeException("Cannot update price of an inactive product");
		}

		// 4. Store old price for event
		BigDecimal oldPrice = BigDecimal.valueOf(product.getPrice());
		BigDecimal newPrice = BigDecimal.valueOf(request.getNewPrice());

		// 5. Update entity
		product.setPrice(request.getNewPrice());
		Product updatedProduct = productRepository.save(product);

		// 6. Publish event
		ProductPriceChangedEvent event = new ProductPriceChangedEvent(
				updatedProduct.getId().toString(),
				oldPrice,
				newPrice);
		eventPublisher.publishProductPriceChanged(event);

		// 7. Return DTO
		return mapToResponse(updatedProduct);
	}

	/**
	 * Deactivate a product (soft delete) and publish a ProductDeactivatedEvent.
	 */
	@Transactional
	public ProductResponse deactivateProduct(UUID productId) {
		// 1. Find product or throw exception
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

		// 2. Check if already inactive
		if (!product.getActive()) {
			throw new RuntimeException("Product is already inactive");
		}

		// 3. Deactivate
		product.setActive(false);
		Product deactivatedProduct = productRepository.save(product);

		// 4. Publish event
		ProductDeactivatedEvent event = new ProductDeactivatedEvent(
				deactivatedProduct.getId().toString());
		eventPublisher.publishProductDeactivated(event);

		// 5. Return DTO
		return mapToResponse(deactivatedProduct);
	}

	/**
	 * Helper method to map Product entity to ProductResponse DTO.
	 */
	private ProductResponse mapToResponse(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getActive());
	}
}
