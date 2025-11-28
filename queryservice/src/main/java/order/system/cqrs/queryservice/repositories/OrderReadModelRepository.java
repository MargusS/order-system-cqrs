package order.system.cqrs.queryservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import order.system.cqrs.queryservice.entities.OrderReadModel;

public interface OrderReadModelRepository extends MongoRepository<OrderReadModel, String> {

	Optional<OrderReadModel> findByOrderId(String orderId);

	List<OrderReadModel> findByStatus(String status);
}
