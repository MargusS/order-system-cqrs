package order.system.cqrs.queryservice.controllers;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import order.system.cqrs.queryservice.entities.ProductReadModel;
import order.system.cqrs.queryservice.services.ProductReadService;

@RestController
@RequestMapping("/queries/products")
public class ProductReadController {

	private ProductReadService productReadService;

	public ProductReadController(ProductReadService productReadService) {
		this.productReadService = productReadService;
	}

	@GetMapping
	public Collection<ProductReadModel> getOrders() {
		return productReadService.getAllProducts();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductReadModel> getOrderById(@PathVariable String id) {
		return productReadService.getProductById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

}
