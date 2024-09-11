package com.gibatekpro.ecommerceandroid.checkout.model;

public class OrderItem {
    private String imageUrl;
    private int quantity;
    private double unitPrice;
    private int productId;

    private OrderItem(Builder builder) {
        this.imageUrl = builder.imageUrl;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
        this.productId = builder.productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "imageUrl='" + imageUrl + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", productId=" + productId +
                '}';
    }

    public static class Builder {
        private String imageUrl;
        private int quantity;
        private double unitPrice;
        private int productId;

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder setProductId(int productId) {
            this.productId = productId;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }

    public OrderItem() {
        // No-args constructor
    }

    public OrderItem(String imageUrl, int quantity, double unitPrice, int productId) {
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.productId = productId;
    }
}
