package com.gibatekpro.ecommerceandroid.checkout.service;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gibatekpro.ecommerceandroid.checkout.callback.PurchaseCallback;
import com.gibatekpro.ecommerceandroid.checkout.model.OrderResponse;
import com.gibatekpro.ecommerceandroid.checkout.model.Purchase;
import com.gibatekpro.ecommerceandroid.util.SsLTrust;
import com.gibatekpro.ecommerceandroid.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckoutService {

    private static final String TAG = "CheckoutService";

    public static void placeOrder(Activity activity, Purchase purchase, PurchaseCallback callback){

        final String orderUrl = Util.apiUrl + "/";

        //My certificate is self-signed, so I need to do this
        //It must be in this order and must not be static
        //TODO: Remove this self-signed certificate in production
        SsLTrust trust = new SsLTrust();
        X509TrustManager trustManager = trust.X509TrustManager(activity);
        SSLContext sslContext = trust.getSslContext();
//
        // Create OkHttp Client
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .hostnameVerifier((hostname, session) -> true)//TODO: Remove this code in production
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(orderUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create API service interface
        CheckoutApiService apiService = retrofit.create(CheckoutApiService.class);

        Call<OrderResponse> call = apiService.placeOrder(purchase);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {

                if (response.isSuccessful()) {

                    OrderResponse orderResponse = response.body();

                    callback.onSuccess(orderResponse);

                } else {
                    // Handle error response
                    callback.onFailure(response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {

                callback.onFailure(t.toString());

            }
        });
    }

}
