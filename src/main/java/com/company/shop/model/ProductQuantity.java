package com.company.shop.model;

import javax.validation.constraints.Positive;

public class ProductQuantity {

    @Positive
    private Long productId;
    @Positive
    private int quantity;

    public ProductQuantity() {
    }

    public ProductQuantity(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
