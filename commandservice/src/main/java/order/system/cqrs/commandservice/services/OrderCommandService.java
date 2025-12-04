package order.system.cqrs.commandservice.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import order.system.cqrs.commandservice.controllers.request.CreateOrderRequest;
import order.system.cqrs.commandservice.controllers.response.OrderResponse;
import order.system.cqrs.commandservice.entities.Order;
import order.system.cqrs.commandservice.entities.OrderItem;
import order.system.cqrs.commandservice.entities.Product;
import order.system.cqrs.commandservice.events.OrderCancelledEvent;
import order.system.cqrs.commandservice.events.OrderConfirmedEvent;
import order.system.cqrs.commandservice.events.OrderCreatedEvent;
import order.system.cqrs.commandservice.events.dto.OrderItemEventDto;
import order.system.cqrs.commandservice.publishers.OrderEventPublisher;
import order.system.cqrs.commandservice.repositories.OrderRepository;
import order.system.cqrs.commandservice.repositories.ProductRepository;

@Service
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderEventPublisher eventPublisher;

    public OrderCommandService(OrderRepository orderRepository, ProductRepository productRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setStatus("PENDING");
        order.setCreatedAt(Instant.now());

        // Process items
        List<OrderItem> items = new ArrayList<>();
        for (var itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

            if (!product.getActive()) {
                throw new RuntimeException("Product is not active: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            order.addItem(orderItem); 
        }

        Order savedOrder = orderRepository.save(order);

        List<OrderItemEventDto> itemDtos = savedOrder.getItems().stream()
                .map(i -> new OrderItemEventDto(i.getProduct().getId().toString(), i.getQuantity(), BigDecimal.valueOf(i.getProduct().getPrice())))
                .collect(Collectors.toList());

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId().toString(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt(),
                itemDtos
        );
        eventPublisher.publishOrderCreated(event);

        return mapToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse confirmOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Order cannot be confirmed. Current status: " + order.getStatus());
        }

        order.setStatus("CONFIRMED");
        orderRepository.save(order);

        eventPublisher.publishOrderConfirmed(new OrderConfirmedEvent(order.getId().toString()));

        return mapToResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(UUID orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("DELIVERY".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot cancel delivery order");
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);

        eventPublisher.publishOrderCancelled(new OrderCancelledEvent(order.getId().toString(), reason));

        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
}

