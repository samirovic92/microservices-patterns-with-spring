package com.samic.ProductsService.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @PostMapping
    public String createProduct() {
        return "Handle Post request";
    }

    @GetMapping
    public String findProduct() {
        return "Handle Get request";
    }

    @PutMapping
    public String updateProduct() {
        return "Handle PUT request";
    }

    @DeleteMapping
    public String deleteProduct() {
        return "Handle Delete request";
    }
}
