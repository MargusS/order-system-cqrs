package order.system.cqrs.commandservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import order.system.cqrs.commandservice.entities.OrderItem;

public interface OrderItemRepository extends CrudRepository<OrderItem, UUID> {

}
