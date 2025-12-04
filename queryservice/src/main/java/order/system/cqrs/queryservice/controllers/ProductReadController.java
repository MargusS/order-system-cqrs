package order.system.cqrs.queryservice.controllers;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import order.system.cqrs.queryservice.controllers.reponse.ProductReadResponse;
import order.system.cqrs.queryservice.mapper.ProductMapper;
import order.system.cqrs.queryservice.services.ProductReadService;

@RestController
@RequestMapping("/queries/products")
public class ProductReadController {

	private final ProductReadService productReadService;
	private final ProductMapper productMapper;

	public ProductReadController(ProductReadService productReadService, ProductMapper productMapper) {
		this.productReadService = productReadService;
		this.productMapper = productMapper;
	}

	@GetMapping
	public ResponseEntity<Collection<ProductReadResponse>> getProducts() {
		return ResponseEntity.ok(productReadService.getAllProducts().stream().map(productMapper::toResponse).toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductReadResponse> getOrderById(@PathVariable String id) {
		return ResponseEntity.of(productReadService.getProductById(id).map(productMapper::toResponse));
	}

}
