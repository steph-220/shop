package com.company.shop.mapper;

import com.company.shop.entity.Product;
import com.company.shop.model.ProductRequest;
import com.company.shop.model.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public Product mapToProduct(ProductRequest productRequest) {
        return new Product(productRequest.getName(), productRequest.getPrice());
    }

    public ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

}
