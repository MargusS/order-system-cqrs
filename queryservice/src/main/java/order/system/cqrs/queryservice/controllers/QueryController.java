package order.system.cqrs.queryservice.controllers;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import order.system.cqrs.queryservice.services.QueryService;

@RestController
@RequestMapping("/queries/orders")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public Collection<String> getOrders() {
        return queryService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable String id) {
        return queryService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
