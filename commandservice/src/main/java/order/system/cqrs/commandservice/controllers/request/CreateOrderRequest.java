package order.system.cqrs.commandservice.controllers.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateOrderRequest {
    // Simple list of items needed to create an order
    private List<OrderItemRequest> items;
}

