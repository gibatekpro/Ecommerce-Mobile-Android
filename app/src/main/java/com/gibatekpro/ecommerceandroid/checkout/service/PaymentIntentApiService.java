package com.gibatekpro.ecommerceandroid.checkout.service;

import com.gibatekpro.ecommerceandroid.checkout.model.PaymentInfo;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentIntentApiService {

    @POST("checkout/payment-intent")
    Call<JsonObject> call(@Body PaymentInfo paymentInfo);

}
