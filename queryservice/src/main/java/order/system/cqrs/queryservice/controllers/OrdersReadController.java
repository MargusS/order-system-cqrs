package order.system.cqrs.queryservice.controllers;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import order.system.cqrs.queryservice.controllers.reponse.OrderReadResponse;
import order.system.cqrs.queryservice.mapper.OrderMapper;
import order.system.cqrs.queryservice.services.OrderReadService;

@RestController
@RequestMapping("/queries/orders")
public class OrdersReadController {

	private final OrderReadService queryService;
	private final OrderMapper orderMapper;

	public OrdersReadController(OrderReadService queryService, OrderMapper orderMapper) {
		this.queryService = queryService;
		this.orderMapper = orderMapper;
	}

	@GetMapping
	public ResponseEntity<Collection<OrderReadResponse>> getOrders() {
		return ResponseEntity.ok(orderMapper.toResponseList(queryService.getAllOrders()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderReadResponse> getOrderById(@PathVariable String id) {
		return ResponseEntity.of(queryService.getOrderById(id).map(orderMapper::toResponse));
	}
}
