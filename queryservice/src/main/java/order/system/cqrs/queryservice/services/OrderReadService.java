package order.system.cqrs.queryservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import order.system.cqrs.queryservice.entities.OrderReadModel;
import order.system.cqrs.queryservice.repositories.OrderReadModelRepository;

@Service
public class OrderReadService {

	private OrderReadModelRepository orderRepository;

	public OrderReadService(OrderReadModelRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<OrderReadModel> getAllOrders() {
		return orderRepository.findAll();
	}

	public Optional<OrderReadModel> getOrderById(String id) {
		return orderRepository.findByOrderId(id);
	}

	private String extractOrderId(String orderJson) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(orderJson);
			return node.get("id").asText(); // asume que hay un campo "id" en JSON
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
