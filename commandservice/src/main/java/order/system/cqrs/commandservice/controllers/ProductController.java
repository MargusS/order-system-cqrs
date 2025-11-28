package order.system.cqrs.commandservice.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import order.system.cqrs.commandservice.controllers.request.CreateProductRequest;
import order.system.cqrs.commandservice.controllers.request.UpdateProductPriceRequest;
import order.system.cqrs.commandservice.controllers.response.ProductResponse;
import order.system.cqrs.commandservice.services.ProductCommandService;

@RestController
@RequestMapping("/api/command/products")
public class ProductController {

    private final ProductCommandService productCommandService;

    public ProductController(ProductCommandService productCommandService) {
        this.productCommandService = productCommandService;
    }

    /**
     * Create a new product.
     * Endpoint: POST /api/command/products
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        ProductResponse response = productCommandService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update product price.
     * Endpoint: PUT /api/command/products/{id}/price
     */
    @PutMapping("/{id}/price")
    public ResponseEntity<ProductResponse> updateProductPrice(
            @PathVariable UUID id,
            @RequestBody UpdateProductPriceRequest request) {
        ProductResponse response = productCommandService.updateProductPrice(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deactivate (soft delete) a product.
     * Endpoint: DELETE /api/command/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deactivateProduct(@PathVariable UUID id) {
        ProductResponse response = productCommandService.deactivateProduct(id);
        return ResponseEntity.ok(response);
    }
}

