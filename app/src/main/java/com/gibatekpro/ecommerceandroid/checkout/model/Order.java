package com.gibatekpro.ecommerceandroid.checkout.model;

public class Order {
    private int totalQuantity;
    private double totalPrice;

    private Order(Builder builder) {
        this.totalQuantity = builder.totalQuantity;
        this.totalPrice = builder.totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "totalQuantity=" + totalQuantity +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public static class Builder {
        private int totalQuantity;
        private double totalPrice;

        public Builder setTotalQuantity(int totalQuantity) {
            this.totalQuantity = totalQuantity;
            return this;
        }

        public Builder setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

    public Order() {
        // No-args constructor
    }

    public Order(int totalQuantity, double totalPrice) {
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }
}
