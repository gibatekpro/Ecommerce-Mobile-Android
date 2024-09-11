package com.gibatekpro.ecommerceandroid.checkout.service;

import com.gibatekpro.ecommerceandroid.checkout.model.OrderResponse;
import com.gibatekpro.ecommerceandroid.checkout.model.Purchase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CheckoutApiService {

    @POST("checkout/purchase")
    Call<OrderResponse> placeOrder(@Body Purchase purchase);

}
