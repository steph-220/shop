package com.company.shop.controller;

import com.company.shop.model.ErrorResponse;
import com.company.shop.model.ProductRequest;
import com.company.shop.model.ProductResponse;
import com.company.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Create new product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New product is created.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class))})})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest product) {

        return ResponseEntity.ok(productService.createProduct(product));
    }


    @Operation(summary = "Update existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existing product is updated.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Product with id 'productId' does not exist.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})})
    @PutMapping(value = "{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("productId") Long productId,
                                                         @Valid @RequestBody ProductRequest product) {

        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }


    @Operation(summary = "Get all products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All products are returned.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))})})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }
}
