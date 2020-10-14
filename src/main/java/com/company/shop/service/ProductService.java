package com.company.shop.service;

import com.company.shop.entity.Product;
import com.company.shop.exception.BadRequestException;
import com.company.shop.mapper.ProductMapper;
import com.company.shop.model.ProductRequest;
import com.company.shop.model.ProductResponse;
import com.company.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest product) {
        Product newProduct = productRepository.save(productMapper.mapToProduct(product));

        return productMapper.mapToProductResponse(newProduct);
    }

    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll().stream()
                .map(e -> productMapper.mapToProductResponse(e))
                .collect(Collectors.toList());
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Product with id " + id + " does not exist."));

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());

        productRepository.save(product);

        return productMapper.mapToProductResponse(product);
    }

}
