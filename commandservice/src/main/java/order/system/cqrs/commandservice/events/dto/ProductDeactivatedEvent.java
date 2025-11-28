package order.system.cqrs.commandservice.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDeactivatedEvent {
    
    private String id; // Product UUID as String
}
