package com.gibatekpro.ecommerceandroid.checkout.service;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gibatekpro.ecommerceandroid.checkout.callback.PaymentIntentCallback;
import com.gibatekpro.ecommerceandroid.checkout.model.OrderResponse;
import com.gibatekpro.ecommerceandroid.checkout.model.PaymentInfo;
import com.gibatekpro.ecommerceandroid.util.SsLTrust;
import com.gibatekpro.ecommerceandroid.util.Util;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.AccessibleObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentIntentService {

    private static final String TAG = "PaymentIntentService";

    private static String baseUrl = Util.apiUrl+"/";

    public static void createPaymentIntent(Activity activity, PaymentInfo paymentInfo, PaymentIntentCallback callback){


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
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        // Create API service interface
        PaymentIntentApiService apiService = retrofit.create(PaymentIntentApiService.class);

        Call<JsonObject> call = apiService.call(paymentInfo);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject gsonObject = response.body();
                    JSONObject orderResponse;
                    try {
                        assert gsonObject != null;
                        orderResponse = new JSONObject(gsonObject.toString());
                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: ==>> " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                    callback.onSuccess(orderResponse);

                } else {
                    // Handle error response
                    callback.onFailure("==>> onResponse" + response.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                callback.onFailure("==>> onFailure" + t.toString());
            }
        });
    }

}
