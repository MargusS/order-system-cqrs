package order.system.cqrs.commandservice.controllers.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

	private UUID id;
	private String name;
	private String description;
	private Double price;
	private Boolean active;
}
