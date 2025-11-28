package order.system.cqrs.commandservice.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductPriceRequest {
    
    private Double newPrice;
}
