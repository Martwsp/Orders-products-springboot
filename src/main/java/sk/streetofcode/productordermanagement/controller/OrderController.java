package sk.streetofcode.productordermanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.streetofcode.productordermanagement.api.OrderService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import sk.streetofcode.productordermanagement.api.request.OrderAddShoppingListRequest;
import sk.streetofcode.productordermanagement.domain.Order;

import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Order>> getAll(){
            return ResponseEntity.ok().body(orderService.getAll());
    }

    @GetMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Order> getById(@PathVariable("id") long id){
        return ResponseEntity.ok().body(orderService.getById(id));
    }


    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created. Returns OrderResponse with assigned id"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Order> add() {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.add());
    }


    @PostMapping("{id}/pay")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order paid. Returns total price. Order is marked as paid"),
            @ApiResponse(responseCode = "400", description = "Order is already paid"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Double> pay(@PathVariable("id") long id) {
     return ResponseEntity.ok().body(orderService.pay(id));
    }


    @PostMapping("{id}/add")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added to order. Returns OrderResponse"),
            @ApiResponse(responseCode = "400", description = "Order is already paid or product is out of stock. No changes made"),
            @ApiResponse(responseCode = "404", description = "Order or product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Order> addShoppingList(@PathVariable("id") long id, @RequestBody OrderAddShoppingListRequest request){
        orderService.addShoppingList(id, request);
        return ResponseEntity.ok().body(orderService.getById(id));
    }


    @DeleteMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        orderService.delete(id);
        return ResponseEntity.ok().build();
    }
}
