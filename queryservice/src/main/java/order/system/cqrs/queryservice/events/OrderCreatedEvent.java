package order.system.cqrs.queryservice.events;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import order.system.cqrs.queryservice.events.dto.OrderItemEventDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
	private String id;
	private String status;
	private Instant createdAt;
	private List<OrderItemEventDto> items; // Una lista simplificada de items
}
