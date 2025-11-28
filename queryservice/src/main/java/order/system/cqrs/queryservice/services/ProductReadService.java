package order.system.cqrs.queryservice.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

import order.system.cqrs.queryservice.entities.ProductReadModel;
import order.system.cqrs.queryservice.repositories.ProductReadModelRepository;

@Service
public class ProductReadService {

	private ProductReadModelRepository productRepository;

	public ProductReadService(ProductReadModelRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Collection<ProductReadModel> getAllProducts() {
		return productRepository.findAll();
	}

	public Optional<ProductReadModel> getProductById(String id) {
		return Optional.ofNullable(productRepository.findByProductId(id).orElse(null));
	}
}
