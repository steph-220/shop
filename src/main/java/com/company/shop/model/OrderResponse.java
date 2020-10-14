package com.company.shop.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private List<ProductQuantity> productsDetails;
    private String buyerEmail;
    private double totalPrice;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductQuantity> getProductsDetails() {
        return productsDetails;
    }

    public void setProductsDetails(List<ProductQuantity> productsDetails) {
        this.productsDetails = productsDetails;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
