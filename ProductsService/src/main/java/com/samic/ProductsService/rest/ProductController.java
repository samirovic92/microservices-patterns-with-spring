package com.samic.ProductsService.rest;

import com.samic.ProductsService.command.CreateProductCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CommandGateway commandGateway;

    public ProductController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createProduct(@RequestBody CreateProductRequest createProductRequest) {
        var createProductCommand = createProductRequest.toCommand();
        String returnValue = this.commandGateway.sendAndWait(createProductCommand);
        return returnValue;
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
