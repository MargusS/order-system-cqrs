package order.system.cqrs.queryservice.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Getters, Setters, toString, etc.
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreatedEvent {
	private String id;
	private String name;
	private String description;
	private BigDecimal price;
}
