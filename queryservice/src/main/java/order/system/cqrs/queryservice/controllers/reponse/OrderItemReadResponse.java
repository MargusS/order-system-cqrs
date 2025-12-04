package order.system.cqrs.queryservice.controllers.reponse;

import java.math.BigDecimal;

public class OrderItemReadResponse {
	public String id;
	public BigDecimal price;
	public Integer quantity;
}
