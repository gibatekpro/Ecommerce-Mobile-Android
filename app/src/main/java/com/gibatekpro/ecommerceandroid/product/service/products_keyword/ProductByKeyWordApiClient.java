package com.gibatekpro.ecommerceandroid.product.service.products_keyword;

import android.app.Activity;

import com.gibatekpro.ecommerceandroid.product.model.ProductResponse;
import com.gibatekpro.ecommerceandroid.util.SsLTrust;
import com.gibatekpro.ecommerceandroid.util.Util;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ProductByKeyWordApiClient {
    // Define APIInterface
    static APIInterface apiInterface;

    // create retrofit instance
    public static APIInterface getAPIInterface(Activity context, String keyWord) {
        if (apiInterface == null) {

            //My certificate is self-signed, so I need to do this
            //It must be in this order and must not be static
            //TODO: Remove this self-signed certificate in production
            SsLTrust trust = new SsLTrust();
            X509TrustManager trustManager = trust.X509TrustManager(context);
            SSLContext sslContext = trust.getSslContext();
//
                // Create OkHttp Client
                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                        .hostnameVerifier((hostname, session) -> true)//TODO: Remove this code in production
                        .build();

                // Create retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Util.apiUrl + "/") // baseUrl must end in "/"
                        .client(client)
                        // Add Gson converter
                        .addConverterFactory(GsonConverterFactory.create())
                        // Add RxJava support for Retrofit
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .build();
                // Init APIInterface
                apiInterface = retrofit.create(APIInterface.class);

        }
        return apiInterface;
    }
    //API service interface
    public interface APIInterface {
        // Define Get request with query string parameter as page number
        @GET("products/search/findByNameContainsIgnoreCase")
        Single<ProductResponse> getProductsByKeyWord(@Query("name") String name, @Query("page") int page);
    }
}
