package order.system.cqrs.queryservice.mapper;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import order.system.cqrs.queryservice.controllers.reponse.OrderItemReadResponse;
import order.system.cqrs.queryservice.controllers.reponse.OrderReadResponse;
import order.system.cqrs.queryservice.entities.OrderItemReadModel;
import order.system.cqrs.queryservice.entities.OrderReadModel;

@Component
public class OrderMapper {

    // Date formatter (ISO-8601 is standard, but customize as needed)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public OrderReadResponse toResponse(OrderReadModel order) {
        if (order == null) {
            return null;
        }

        OrderReadResponse response = new OrderReadResponse();
        
        // Map basic fields
        // Use the business ID (orderId) for the client, not the internal Mongo ID (id)
        response.id = order.getOrderId(); 
        response.status = order.getStatus();
        
        // Format Date
        if (order.getCreatedAt() != null) {
            response.orderDate = DATE_FORMATTER.format(order.getCreatedAt());
        }

        // Map Products (Items)
        List<OrderItemReadResponse> products = mapItems(order.getItems());
        response.products = products;

        // Calculate Total Amount
        // Logic: Sum of (Price * Quantity) for all items
        response.totalAmount = calculateTotal(products);

        return response;
    }

    private List<OrderItemReadResponse> mapItems(List<OrderItemReadModel> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        return items.stream()
            .map(item -> {
                OrderItemReadResponse productResp = new OrderItemReadResponse();
                productResp.id = item.getProductId();
                productResp.price = item.getPrice();  // This is unit price
								productResp.quantity = item.getQuantity();
                return productResp;
            })
            .collect(Collectors.toList());
    }

    private BigDecimal calculateTotal(List<OrderItemReadResponse> products) {
        if (products == null) {
            return BigDecimal.ZERO;
        }
        
        return products.stream()
            .map(p -> {
                BigDecimal price = p.price != null ? p.price : BigDecimal.ZERO;
                BigDecimal qty = BigDecimal.valueOf(p.quantity != null ? p.quantity : 0);
                return price.multiply(qty);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Helper for lists
    public Collection<OrderReadResponse> toResponseList(List<OrderReadModel> orders) {
        if (orders == null) {
            return Collections.emptyList();
        }
        return orders.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
}
