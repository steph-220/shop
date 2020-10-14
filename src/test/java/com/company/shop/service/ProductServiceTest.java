package com.company.shop.service;

import com.company.shop.entity.Product;
import com.company.shop.exception.BadRequestException;
import com.company.shop.mapper.ProductMapper;
import com.company.shop.model.ProductRequest;
import com.company.shop.model.ProductResponse;
import com.company.shop.repository.ProductRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Spy
    private ProductMapper productMapper;

    private ProductRequest productRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest();
        productRequest.setName(RandomString.make());
        productRequest.setPrice(100.2);

        product = new Product();
        product.setId(1L);
        product.setName(RandomString.make());
        product.setPrice(98.4);
    }

    @Test
    void createProduct_productIsCreated() {
        Mockito.when(productRepository.save(ArgumentMatchers.any(Product.class)))
                .then(AdditionalAnswers.returnsFirstArg());


        ProductResponse productResponse = productService.createProduct(productRequest);


        Mockito.verify(productRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(Product.class));
        assertEquals(productRequest.getName(), productResponse.getName());
        assertEquals(productRequest.getPrice(), productResponse.getPrice());
    }

    @Test
    void getAllProducts_productFound() {

        Mockito.when(productRepository.findAll())
                .thenReturn(Collections.singletonList(product));


        List<ProductResponse> allProducts = productService.getAllProducts();


        assertEquals(1, allProducts.size());
        assertEquals(product.getId(), allProducts.get(0).getId());
        assertEquals(product.getName(), allProducts.get(0).getName());
        assertEquals(product.getPrice(), allProducts.get(0).getPrice());
    }

    @Test
    void updateProduct_productDoesNotExist_throwBadRequest() {

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> productService.updateProduct(1L, productRequest));


        assertEquals("Product with id 1 does not exist.", badRequestException.getMessage());
    }

    @Test
    void updateProduct_productDoesExist_productUpdated() {

        Mockito.when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(ArgumentMatchers.any(Product.class)))
                .then(AdditionalAnswers.returnsFirstArg());


        ProductResponse productResponse = productService.updateProduct(product.getId(), productRequest);


        assertEquals(product.getId(), productResponse.getId());
        assertEquals(productRequest.getName(), productResponse.getName());
        assertEquals(productRequest.getPrice(), productResponse.getPrice());
    }
}