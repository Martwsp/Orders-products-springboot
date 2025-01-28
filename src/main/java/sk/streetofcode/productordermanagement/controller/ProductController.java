package sk.streetofcode.productordermanagement.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.streetofcode.productordermanagement.api.ProductService;
import sk.streetofcode.productordermanagement.api.request.ProductAddAmountRequest;
import sk.streetofcode.productordermanagement.api.request.ProductAddRequest;
import sk.streetofcode.productordermanagement.api.request.ProductEditRequest;
import sk.streetofcode.productordermanagement.domain.Product;
import sk.streetofcode.productordermanagement.domain.ProductAmount;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
   public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok().body(productService.getAll());
   }

    @GetMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
   public ResponseEntity<Product> get(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(productService.get(id));
   }

    @GetMapping("{id}/amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductAmount> getAmount(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(new ProductAmount(productService.getAmount(id)));
    }

    @GetMapping("{id}/price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Double> getPrice(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(productService.getPrice(id));
    }


    @PutMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> edit(@PathVariable("id") long id, @RequestBody ProductEditRequest request) {
        return ResponseEntity.ok().body(productService.edit(id, request));
    }


    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created."),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> add(@RequestBody ProductAddRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.add(request));
    }


    @PostMapping("{id}/amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product amount updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductAmount> addAmount(@PathVariable("id") long id, @RequestBody ProductAddAmountRequest request) {
        return ResponseEntity.ok().body(new ProductAmount (productService.addAmount(id, request)));
    }



    @DeleteMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        productService.delete(id);
        return ResponseEntity.ok().build();
    }


}
