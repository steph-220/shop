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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Spy
    private OrderMapper orderMapper;

    private OrderRequest orderRequest;
    private ProductQuantity product1Quantity1;
    private ProductQuantity product2Quantity3;


    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest();
        orderRequest.setBuyerEmail(RandomString.make());
        orderRequest.setProductsDetails(new HashSet<>());
        product1Quantity1 = populateProductQuantity(1L, 1);
        product2Quantity3 = populateProductQuantity(2L, 3);
    }

    @Test
    void placeOrder_productWithSameIdTwice_throwBadRequest() {
        orderRequest.getProductsDetails().add(product1Quantity1);
        product2Quantity3.setProductId(product1Quantity1.getProductId());
        orderRequest.getProductsDetails().add(product2Quantity3);


        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> orderService.placeOrder(orderRequest));


        Mockito.verify(orderRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(OrderPlacement.class));
        assertEquals("Products ids must be unique.", badRequestException.getMessage());
    }

    @Test
    void placeOrder_productDoesNotExist_throwBadRequest() {
        orderRequest.getProductsDetails().add(product1Quantity1);


        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> orderService.placeOrder(orderRequest));


        Mockito.verify(orderRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(OrderPlacement.class));
        assertEquals("Products with ids [1] does not exists.", badRequestException.getMessage());
    }

    @Test
    void placeOrder_orderRequestValid_productIsCreated() {
        orderRequest.getProductsDetails().add(product1Quantity1);
        orderRequest.getProductsDetails().add(product2Quantity3);

        Product p1 = populateProduct(product1Quantity1, 10.4);
        Product p2 = populateProduct(product2Quantity3, 2.0);

        Mockito.when(productRepository.findAllById(new HashSet<>(Arrays.asList(product1Quantity1.getProductId(), product2Quantity3.getProductId()))))
                .thenReturn(Arrays.asList(p1, p2));
        Mockito.when(orderRepository.save(ArgumentMatchers.any(OrderPlacement.class)))
                .then(AdditionalAnswers.returnsFirstArg());


        OrderResponse orderResponse = orderService.placeOrder(orderRequest);


        Mockito.verify(orderRepository, Mockito.times(2))
                .save(ArgumentMatchers.any(OrderPlacement.class));
        assertEquals(orderRequest.getBuyerEmail(), orderResponse.getBuyerEmail());
        assertEquals(16.4, orderResponse.getTotalPrice());
        assertEquals(2, orderResponse.getProductsDetails().size());
    }

    @Test
    void getAllOrdersBetween_orderFound() {
        LocalDateTime now = LocalDateTime.now();
        OrderPlacement orderPlacement = new OrderPlacement();
        orderPlacement.setId(100L);
        orderPlacement.setBuyerEmail(RandomString.make());
        orderPlacement.setTotalPrice(100.2);
        orderPlacement.setCreatedAt(now.minusDays(1));
        Product p1 = populateProduct(product1Quantity1, 100.2);
        OrderProduct orderProduct = new OrderProduct(orderPlacement, p1, product1Quantity1.getQuantity());
        orderPlacement.setProducts(Collections.singletonList(orderProduct));

        Mockito.when(orderRepository.getByCreatedAtBetween(now.minusDays(2), now))
                .thenReturn(Collections.singletonList(orderPlacement));


        List<OrderResponse> allOrdersBetween = orderService.getAllOrdersBetween(now.minusDays(2), now);


        assertEquals(1, allOrdersBetween.size());
        assertEquals(orderPlacement.getId(), allOrdersBetween.get(0).getId());
        assertEquals(orderPlacement.getBuyerEmail(), allOrdersBetween.get(0).getBuyerEmail());
        assertEquals(orderPlacement.getTotalPrice(), allOrdersBetween.get(0).getTotalPrice());
        assertEquals(orderPlacement.getCreatedAt(), allOrdersBetween.get(0).getCreatedAt());

    }

    private ProductQuantity populateProductQuantity(Long productId, int quantity) {
        ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProductId(productId);
        productQuantity.setQuantity(quantity);

        return productQuantity;
    }

    private Product populateProduct(ProductQuantity productQuantity, Double price) {
        Product product = new Product();
        product.setId(productQuantity.getProductId());
        product.setName(RandomString.make());
        product.setPrice(price);

        return product;
    }
}