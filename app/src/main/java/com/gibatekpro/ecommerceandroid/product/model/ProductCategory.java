package com.gibatekpro.ecommerceandroid.product.model;

public class ProductCategory {

    private final long id;

    private final String categoryName;

    public ProductCategory(long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
