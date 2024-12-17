package com.samic.ProductsService.command.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        var createProductCommand = createProductRequest.toCommand();
        String returnValue = this.commandGateway.sendAndWait(createProductCommand);
        return returnValue;
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
