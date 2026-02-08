package com.example.lab3.order;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final List<Order> orders = new ArrayList<>();
    private long idCounter = 1;

    // Create
    @PostMapping
    public Order create(@RequestBody Order order) {
        order.id = idCounter++;
        orders.add(order);
        return order;
    }

    // Get all
    @GetMapping
    public List<Order> getAll() {
        return orders;
    }

    // Get by id
    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orders.stream()
                .filter(o -> o.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update all fields
    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @RequestBody Order order) {
        Order existing = getById(id);
        if (existing == null) return null;

        existing.productName = order.productName;
        existing.quantity = order.quantity;
        existing.status = order.status;
        return existing;
    }

    // Extra: update ONLY status
    @PatchMapping("/{id}/status")
    public Order updateStatus(
            @PathVariable Long id,
            @RequestBody String status
    ) {
        Order existing = getById(id);
        if (existing == null) return null;

        existing.status = status;
        return existing;
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orders.removeIf(o -> o.id.equals(id));
    }

    // Extra: get orders by status
    @GetMapping("/status/{status}")
    public List<Order> getByStatus(@PathVariable String status) {
        return orders.stream()
                .filter(o -> o.status.equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}
