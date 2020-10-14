package com.company.shop.model;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class OrderRequest {

    @NotEmpty
    @Valid
    private Set<ProductQuantity> productsDetails;

    @Email
    @NotNull
    private String buyerEmail;

    public Set<ProductQuantity> getProductsDetails() {
        return productsDetails;
    }

    public void setProductsDetails(Set<ProductQuantity> productsDetails) {
        this.productsDetails = productsDetails;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }
}
