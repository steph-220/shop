package com.company.shop.mapper;

import com.company.shop.entity.OrderPlacement;
import com.company.shop.model.OrderRequest;
import com.company.shop.model.OrderResponse;
import com.company.shop.model.ProductQuantity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMapper {

    public OrderPlacement mapToOrderPlacement(OrderRequest orderRequest, double totalPrice) {
        OrderPlacement orderPlacement = new OrderPlacement();
        orderPlacement.setBuyerEmail(orderRequest.getBuyerEmail());
        orderPlacement.setCreatedAt(LocalDateTime.now());
        orderPlacement.setTotalPrice(totalPrice);

        return orderPlacement;
    }

    public OrderResponse mapToOrderResponse(OrderPlacement orderPlacement) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(orderPlacement.getId());
        orderResponse.setBuyerEmail(orderPlacement.getBuyerEmail());
        orderResponse.setTotalPrice(orderPlacement.getTotalPrice());
        orderResponse.setCreatedAt(orderPlacement.getCreatedAt());

        List<ProductQuantity> productResponses = orderPlacement.getProducts().stream()
                .map(e -> new ProductQuantity(e.getId().getProductId(), e.getQuantity()))
                .collect(Collectors.toList());
        orderResponse.setProductsDetails(productResponses);

        return orderResponse;
    }

}
