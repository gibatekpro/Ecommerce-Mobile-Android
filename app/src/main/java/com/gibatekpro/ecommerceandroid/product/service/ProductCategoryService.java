package com.gibatekpro.ecommerceandroid.product.service;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gibatekpro.ecommerceandroid.product.callback.ProductCategoryCallback;
import com.gibatekpro.ecommerceandroid.product.model.ProductCategory;
import com.gibatekpro.ecommerceandroid.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryService {

    private static final String TAG = "Product Category";

    public static void fetchProductCategories(Activity context, ProductCategoryCallback callback) {


        final String fetchProductsUrl = Util.apiUrl + "/product-categories";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                fetchProductsUrl,
                null,
                response -> {
                    try {

                        List<ProductCategory> productCategories = new ArrayList<>();

                        //We need to get the _embedded object first because the products array
                        //is nested within it
                        JSONObject embedded = response.getJSONObject("_embedded");

                        //Now, we can get the products array
                        JSONArray productsArray = embedded.getJSONArray("productCategory");

                        for (int i = 0; i < productsArray.length(); i++) {
                            JSONObject jsonProduct = productsArray.getJSONObject(i);
                            int id = jsonProduct.getInt("id");
                            String name = jsonProduct.getString("categoryName");

                            //add the products to the array
                            productCategories.add(new ProductCategory(id, name));
                        }

                        //We are using a callback because the return statement is executed before
                        //the fetch response is complete
                        callback.onSuccess(productCategories);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Stack Trace: " + e);
                        callback.onFailure(e.getMessage());
                    }
                }, error -> callback.onFailure(error.getMessage())
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

}
