package order.system.cqrs.commandservice.controllers.request;

import java.util.UUID;

import lombok.Data;

@Data
public class OrderItemRequest {
    private UUID productId;
    private Integer quantity;
}
