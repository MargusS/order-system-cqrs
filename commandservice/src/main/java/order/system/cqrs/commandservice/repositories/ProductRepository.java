package order.system.cqrs.commandservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import order.system.cqrs.commandservice.entities.Product;

public interface ProductRepository extends CrudRepository<Product, UUID> {
	
}
