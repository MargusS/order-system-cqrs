package order.system.cqrs.queryservice.entities;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "orders")
@Getter
@Setter
public class OrderReadModel {
	@Id
	private String id;
	@Indexed(unique = true)
	private String orderId;
	private String status;
	private Instant createdAt;
	private List<OrderItemReadModel> items;
}
