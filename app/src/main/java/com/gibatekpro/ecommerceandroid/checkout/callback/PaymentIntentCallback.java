package com.gibatekpro.ecommerceandroid.checkout.callback;

import com.gibatekpro.ecommerceandroid.checkout.model.OrderResponse;

import org.json.JSONObject;

public interface PaymentIntentCallback {

    void onSuccess(JSONObject intentResponse);

    void onFailure(String error);

}
