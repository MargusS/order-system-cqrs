package order.system.cqrs.queryservice.entities;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "products")
@Getter
@Setter
public class ProductReadModel {
	@Id
	private String id;
	private String productId;
	private String name;
	private String description;
	private BigDecimal price;
}