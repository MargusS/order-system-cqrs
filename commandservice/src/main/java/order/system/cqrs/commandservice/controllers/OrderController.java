package order.system.cqrs.commandservice.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import order.system.cqrs.commandservice.controllers.request.CreateOrderRequest;
import order.system.cqrs.commandservice.controllers.response.OrderResponse;
import order.system.cqrs.commandservice.services.OrderCommandService;

@RestController
@RequestMapping("/api/command/orders")
public class OrderController {

	private final OrderCommandService orderService;

	public OrderController(OrderCommandService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
		return ResponseEntity.ok(orderService.createOrder(request));
	}

	@PutMapping("/{id}/confirm")
	public ResponseEntity<OrderResponse> confirmOrder(@PathVariable UUID id) {
		return ResponseEntity.ok(orderService.confirmOrder(id));
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID id,
			@RequestBody(required = false) Map<String, String> body) {
		String reason = body != null ? body.get("reason") : "No reason provided";
		return ResponseEntity.ok(orderService.cancelOrder(id, reason));
	}
}
