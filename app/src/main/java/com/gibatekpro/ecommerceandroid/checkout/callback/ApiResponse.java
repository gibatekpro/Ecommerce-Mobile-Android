package com.gibatekpro.ecommerceandroid.checkout.callback;

public interface ApiResponse {

    void onSuccess(String orderTrackingNumber);

    void onFailure(String error);

}
