package ca.gbc.productservice.controller;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // A derivative of controller, but concerned with JSON or XML
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // If succeeds, status code 201
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {

        ProductResponse createdProduct = productService.createProduct(productRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/product/" + createdProduct.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdProduct);
    }


    // READ
    @GetMapping
    @ResponseStatus(HttpStatus.OK) // If succeeds, status code 200
    public List<ProductResponse> getAllProducts(){

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return productService.getAllProducts();

    }


    // UPDATE
    // http://localhost:8080/api/product/{primaryKey}
    @PutMapping("/{productId}") // If succeeds, status code 204
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateProduct(@PathVariable("productId") String productId,
                                           @RequestBody ProductRequest productRequest) {
        String updatedProductId = productService.updateProduct(productId, productRequest);

        // Set the location header attribute
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/product/" + updatedProductId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }


    // DELETE
    @DeleteMapping("/{productId}") // If succeeds, status code 204
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
