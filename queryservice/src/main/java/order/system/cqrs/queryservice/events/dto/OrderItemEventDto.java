package order.system.cqrs.queryservice.events.dto;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEventDto {
    private String productId;
    private Integer quantity;
	private BigDecimal price;
}
