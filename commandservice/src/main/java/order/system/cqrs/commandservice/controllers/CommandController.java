package order.system.cqrs.commandservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import order.system.cqrs.commandservice.services.CommandService;

@RestController
@RequestMapping("/commands/orders")
public class CommandController {
   
    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody String orderJson) {
        commandService.createOrder(orderJson);
        return ResponseEntity.ok("Order created");
    }
}
