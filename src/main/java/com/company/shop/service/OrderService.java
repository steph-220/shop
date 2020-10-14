package com.company.shop.service;

import com.company.shop.entity.OrderPlacement;
import com.company.shop.entity.OrderProduct;
import com.company.shop.entity.Product;
import com.company.shop.exception.BadRequestException;
import com.company.shop.mapper.OrderMapper;
import com.company.shop.model.OrderRequest;
import com.company.shop.model.OrderResponse;
import com.company.shop.model.ProductQuantity;
import com.company.shop.repository.OrderRepository;
import com.company.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderMapper orderMapper;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        if (!allProductIdsUnique(orderRequest.getProductsDetails())) {
            throw new BadRequestException("Products ids must be unique.");
        }

        List<Product> products = productRepository.findAllById(orderRequest.getProductsDetails().stream()
                .map(ProductQuantity::getProductId)
                .collect(Collectors.toSet()));
        Set<Long> missingProductIds = getMissingProductIds(products, orderRequest.getProductsDetails());
        if (!missingProductIds.isEmpty()) {
            throw new BadRequestException("Products with ids " + missingProductIds + " does not exists.");
        }

        Map<Long, Integer> quantityByProductId = orderRequest.getProductsDetails().stream()
                .collect(Collectors.toMap(ProductQuantity::getProductId, ProductQuantity::getQuantity));

        BigDecimal totalPrice = products.stream()
                .map(e -> BigDecimal.valueOf(e.getPrice()).multiply(BigDecimal.valueOf(quantityByProductId.get(e.getId()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderPlacement orderPlacement = saveOrderPlacement(orderRequest, totalPrice.doubleValue(), products);

        return orderMapper.mapToOrderResponse(orderPlacement);
    }

    public List<OrderResponse> getAllOrdersBetween(LocalDateTime from, LocalDateTime to) {

        List<OrderPlacement> byCreatedAtBetween = orderRepository.getByCreatedAtBetween(from, to);

        return byCreatedAtBetween.stream()
                .map(e -> orderMapper.mapToOrderResponse(e))
                .collect(Collectors.toList());
    }

    private OrderPlacement saveOrderPlacement(OrderRequest orderRequest, Double totalPrice, List<Product> products) {
        OrderPlacement orderPlacement = orderRepository.save(orderMapper.mapToOrderPlacement(orderRequest, totalPrice));

        Map<Long, Product> productById = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        List<OrderProduct> orderProducts = orderRequest.getProductsDetails().stream()
                .map(e -> new OrderProduct(orderPlacement, productById.get(e.getProductId()), e.getQuantity()))
                .collect(Collectors.toList());

        orderPlacement.setProducts(orderProducts);
        return orderRepository.save(orderPlacement);
    }

    private boolean allProductIdsUnique(Set<ProductQuantity> productQuantities) {
        Set<Long> uniqueRequestProductsIds = productQuantities.stream()
                .map(ProductQuantity::getProductId)
                .collect(Collectors.toSet());

        return productQuantities.size() == uniqueRequestProductsIds.size();
    }

    private Set<Long> getMissingProductIds(List<Product> existingProducts, Set<ProductQuantity> productQuantities) {
        Set<Long> existingProductIds = existingProducts.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
        return productQuantities.stream()
                .map(ProductQuantity::getProductId)
                .filter(e -> !existingProductIds.contains(e))
                .collect(Collectors.toSet());
    }

}
