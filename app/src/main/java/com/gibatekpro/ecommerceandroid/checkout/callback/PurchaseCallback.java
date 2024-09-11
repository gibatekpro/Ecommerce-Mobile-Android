package com.gibatekpro.ecommerceandroid.checkout.callback;

import com.gibatekpro.ecommerceandroid.checkout.model.OrderResponse;

public interface PurchaseCallback {


    void onSuccess(OrderResponse orderResponse);

    void onFailure(String error);


}
