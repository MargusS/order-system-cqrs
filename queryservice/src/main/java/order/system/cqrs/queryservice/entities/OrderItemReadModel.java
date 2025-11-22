package order.system.cqrs.queryservice.entities;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemReadModel {
	private String productId;
	private String productName;
	private Integer quantity;
	private BigDecimal price;
}