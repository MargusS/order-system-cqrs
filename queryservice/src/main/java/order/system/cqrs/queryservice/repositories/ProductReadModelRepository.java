package order.system.cqrs.queryservice.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import order.system.cqrs.queryservice.entities.ProductReadModel;

public interface ProductReadModelRepository extends MongoRepository<ProductReadModel, String> {
	Optional<ProductReadModel> findByProductId(String productId);
}
