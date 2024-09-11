package com.gibatekpro.ecommerceandroid.checkout.model;

public class OrderResponse {

    String orderTrackingNumber;

    public OrderResponse(String orderTrackingNumber) {
        this.orderTrackingNumber = orderTrackingNumber;
    }

    public OrderResponse() {
    }

    public String getOrderTrackingNumber() {
        return orderTrackingNumber;
    }

    public void setOrderTrackingNumber(String orderTrackingNumber) {
        this.orderTrackingNumber = orderTrackingNumber;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderTrackingNumber='" + orderTrackingNumber + '\'' +
                '}';
    }
}
