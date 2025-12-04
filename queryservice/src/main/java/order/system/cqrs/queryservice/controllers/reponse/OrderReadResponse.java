package order.system.cqrs.queryservice.controllers.reponse;

import java.math.BigDecimal;
import java.util.List;

public class OrderReadResponse {
	public String id;
	public String orderDate;
	public String status;
	public BigDecimal totalAmount;
	public List<OrderItemReadResponse> products;
}
