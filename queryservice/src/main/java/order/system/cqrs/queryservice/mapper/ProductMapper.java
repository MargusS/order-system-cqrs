package order.system.cqrs.queryservice.mapper;

import org.springframework.stereotype.Component;

import order.system.cqrs.queryservice.controllers.reponse.ProductReadResponse;
import order.system.cqrs.queryservice.entities.ProductReadModel;

@Component
public class ProductMapper {
	
	public ProductReadResponse toResponse(ProductReadModel product) {
		if (product == null) {
			return null;
		}
		ProductReadResponse response = new ProductReadResponse();
		response.id = product.getProductId();
		response.name = product.getName();
		response.price = product.getPrice();
		return response;
	}
}
