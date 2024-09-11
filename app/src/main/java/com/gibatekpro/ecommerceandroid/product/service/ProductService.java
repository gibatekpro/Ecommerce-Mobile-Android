package com.gibatekpro.ecommerceandroid.product.service;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gibatekpro.ecommerceandroid.product.callback.ProductCallback;
import com.gibatekpro.ecommerceandroid.util.Util;
import com.gibatekpro.ecommerceandroid.product.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductService{

    private static final String TAG = "Product";


    // Define the date format used in the JSON object
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void fetchProducts(Activity context, ProductCallback callback) {


        final String fetchProductsUrl = Util.apiUrl + "/products";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                fetchProductsUrl,
                null,
                response -> {
                    try {

                        List<Product> products = new ArrayList<>();

                        //We need to get the _embedded object first because the products array
                        //is nested within it
                        JSONObject embedded = response.getJSONObject("_embedded");

                        //Now, we can get the products array
                        JSONArray productsArray = embedded.getJSONArray("products");

                        for (int i = 0; i < productsArray.length(); i++) {
                            JSONObject jsonProduct = productsArray.getJSONObject(i);
                            int id = jsonProduct.getInt("id");
                            String sku = jsonProduct.getString("sku");
                            String name = jsonProduct.getString("name");
                            String description = jsonProduct.getString("description");
                            BigDecimal unitPrice = BigDecimal.valueOf(jsonProduct.getDouble("unitPrice"));
                            String imageUrl = jsonProduct.getString("imageUrl");
                            boolean active = jsonProduct.getBoolean("active");
                            int unitsInStock = jsonProduct.getInt("unitsInStock");
                            // Parse the strings into Date objects
                            Date dateCreated = null;
                            Date lastUpdated = null;
                            try {
                                dateCreated = dateFormat.parse(jsonProduct.getString("dateCreated"));
                                lastUpdated = dateFormat.parse(jsonProduct.getString("lastUpdated"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                // Handle the parsing exception
                            }

                            //add the products to the array
                            products.add(new Product(id, sku, name, description, unitPrice, imageUrl, active, unitsInStock, dateCreated, lastUpdated));
                        }

                        //We are using a callback because the return statement is executed before
                        //the fetch response is complete
                        callback.onSuccess(products);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Stack Trace: " + e);
                    }
                }, error -> Log.d(TAG, "Error: " + error)
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public void fetchProductsByProductsCategory(Activity context, ProductCallback callback, int categoryId) {


        final String fetchProductsUrl = Util.apiUrl + "/products/search/findByProductCategoryId?id="+ categoryId;


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                fetchProductsUrl,
                null,
                response -> {
                    try {

                        List<Product> products = new ArrayList<>();

                        //We need to get the _embedded object first because the products array
                        //is nested within it
                        JSONObject embedded = response.getJSONObject("_embedded");

                        //Now, we can get the products array
                        JSONArray productsArray = embedded.getJSONArray("products");

                        for (int i = 0; i < productsArray.length(); i++) {
                            JSONObject jsonProduct = productsArray.getJSONObject(i);
                            int id = jsonProduct.getInt("id");
                            String sku = jsonProduct.getString("sku");
                            String name = jsonProduct.getString("name");
                            String description = jsonProduct.getString("description");
                            BigDecimal unitPrice = BigDecimal.valueOf(jsonProduct.getDouble("unitPrice"));
                            String imageUrl = jsonProduct.getString("imageUrl");
                            boolean active = jsonProduct.getBoolean("active");
                            int unitsInStock = jsonProduct.getInt("unitsInStock");
                            // Parse the strings into Date objects
                            Date dateCreated = null;
                            Date lastUpdated = null;
                            try {
                                dateCreated = dateFormat.parse(jsonProduct.getString("dateCreated"));
                                lastUpdated = dateFormat.parse(jsonProduct.getString("lastUpdated"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                // Handle the parsing exception
                            }

                            //add the products to the array
                            products.add(new Product(id, sku, name, description, unitPrice, imageUrl, active, unitsInStock, dateCreated, lastUpdated));
                        }

                        //We are using a callback because the return statement is executed before
                        //the fetch response is complete
                        callback.onSuccess(products);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Stack Trace: " + e);
                    }
                }, error -> Log.d(TAG, "Error: " + error)
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

}
