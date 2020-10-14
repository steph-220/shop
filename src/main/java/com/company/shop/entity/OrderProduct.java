package com.company.shop.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.util.Objects;

@Entity
public class OrderProduct {

    @EmbeddedId
    private OrderProductPK id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "orderId")
    private OrderPlacement orderPlacement;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productId")
    private Product product;

    private int quantity;

    public OrderProduct() {
    }

    public OrderProduct(OrderPlacement orderPlacement, Product product, int quantity) {
        this.id = new OrderProductPK(orderPlacement.getId(), product.getId());
        this.orderPlacement = orderPlacement;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderProductPK getId() {
        return id;
    }

    public void setId(OrderProductPK id) {
        this.id = id;
    }

    public OrderPlacement getOrderPlacement() {
        return orderPlacement;
    }

    public void setOrderPlacement(OrderPlacement orderPlacement) {
        this.orderPlacement = orderPlacement;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return quantity == that.quantity &&
                Objects.equals(id, that.id) &&
                Objects.equals(orderPlacement, that.orderPlacement) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderPlacement, product, quantity);
    }
}
