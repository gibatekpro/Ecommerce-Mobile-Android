package com.gibatekpro.ecommerceandroid.checkout.model;

import java.util.List;

public class Purchase {
    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private List<OrderItem> orderItems;

    private Purchase(Builder builder) {
        this.customer = builder.customer;
        this.shippingAddress = builder.shippingAddress;
        this.billingAddress = builder.billingAddress;
        this.order = builder.order;
        this.orderItems = builder.orderItems;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public Order getOrder() {
        return order;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "customer=" + customer +
                ", shippingAddress=" + shippingAddress +
                ", billingAddress=" + billingAddress +
                ", order=" + order +
                ", orderItems=" + orderItems +
                '}';
    }

    public static class Builder {
        private Customer customer;
        private Address shippingAddress;
        private Address billingAddress;
        private Order order;
        private List<OrderItem> orderItems;

        public Builder() {
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder setShippingAddress(Address shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder setBillingAddress(Address billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public Builder setOrder(Order order) {
            this.order = order;
            return this;
        }

        public Builder setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public Purchase build() {
            return new Purchase(this);
        }
    }
}
