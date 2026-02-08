package com.example.lab3.product;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final List<Product> products = new ArrayList<>();
    private long idCounter = 1;

    // Create
    @PostMapping
    public Product create(@RequestBody Product product) {
        product.id = idCounter++;
        products.add(product);
        return product;
    }

    // Get all
    @GetMapping
    public List<Product> getAll() {
        return products;
    }

    // Get by id
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return products.stream()
                .filter(p -> p.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        Product existing = getById(id);
        if (existing == null) return null;

        existing.name = product.name;
        existing.price = product.price;
        return existing;
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        products.removeIf(p -> p.id.equals(id));
    }

    // Extra: get products with price greater than value
    @GetMapping("/price/greater/{value}")
    public List<Product> getWithPriceGreaterThan(@PathVariable double value) {
        return products.stream()
                .filter(p -> p.price > value)
                .collect(Collectors.toList());
    }
}
