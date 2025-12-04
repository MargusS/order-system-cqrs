package order.system.cqrs.commandservice.controllers.response;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderResponse {
    private UUID id;
    private String status;
    private Instant createdAt;
    // Podríamos devolver items o totalAmount si quisiéramos ser más detallados
}
