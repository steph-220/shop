package com.company.shop.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class ProductRequest {

    @NotBlank
    private String name;
    @PositiveOrZero
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
