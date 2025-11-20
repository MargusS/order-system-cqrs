package order.system.cqrs.queryservice.services;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QueryService {

	private final Map<String, String> orders = new ConcurrentHashMap<>();

	@KafkaListener(topics = "orders", groupId = "orders-group")
	public void listen(String orderJson) {
		// Aquí parsearías orderJson a objeto, pero para ejemplo simple guardamos como
		// cadena
		// Para simplificar, usamos un id variable en el JSON
		String orderId = extractOrderId(orderJson); // Implementa extracción JSON real
		orders.put(orderId, orderJson);
		System.out.println("Order updated: " + orderJson);
	}

	public Collection<String> getAllOrders() {
		return orders.values();
	}

	public Optional<String> getOrderById(String id) {
		return Optional.ofNullable(orders.get(id));
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
