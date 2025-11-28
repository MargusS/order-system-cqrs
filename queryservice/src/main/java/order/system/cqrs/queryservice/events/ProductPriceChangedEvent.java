package order.system.cqrs.queryservice.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceChangedEvent {
	private String id;
	private BigDecimal oldPrice;
	private BigDecimal newPrice;
}
