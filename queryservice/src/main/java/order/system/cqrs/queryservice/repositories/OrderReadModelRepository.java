package order.system.cqrs.queryservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import order.system.cqrs.queryservice.entities.OrderReadModel;

public interface OrderReadModelRepository extends MongoRepository<OrderReadModel, String> {

	// Find order by its business ID (the UUID string coming from Command Service)
	Optional<OrderReadModel> findByOrderId(String orderId);

	// Example: Find all orders with a specific status
	List<OrderReadModel> findByStatus(String status);
}
