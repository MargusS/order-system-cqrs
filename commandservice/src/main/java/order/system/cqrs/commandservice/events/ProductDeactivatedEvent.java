package order.system.cqrs.commandservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDeactivatedEvent {
    
    private String id; // Product UUID as String
}
