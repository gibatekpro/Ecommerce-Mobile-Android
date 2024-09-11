package com.gibatekpro.ecommerceandroid.checkout.service;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gibatekpro.ecommerceandroid.checkout.callback.CountryCallback;
import com.gibatekpro.ecommerceandroid.checkout.callback.StateCallback;
import com.gibatekpro.ecommerceandroid.checkout.model.Country;
import com.gibatekpro.ecommerceandroid.checkout.model.State;
import com.gibatekpro.ecommerceandroid.product.callback.ProductCategoryCallback;
import com.gibatekpro.ecommerceandroid.product.model.ProductCategory;
import com.gibatekpro.ecommerceandroid.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryStateService {

    private static final String TAG = "CountryStateService";

    public static void fetchCountries(Activity context, CountryCallback callback) {


        final String fetchCountriesUrl = Util.apiUrl + "/countries";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                fetchCountriesUrl,
                null,
                response -> {
                    try {

                        List<Country> countries = new ArrayList<>();

                        //We need to get the _embedded object first because the products array
                        //is nested within it
                        JSONObject embedded = response.getJSONObject("_embedded");

                        //Now, we can get the products array
                        JSONArray countriesArray = embedded.getJSONArray("countries");

                        for (int i = 0; i < countriesArray.length(); i++) {
                            JSONObject jsonCountry = countriesArray.getJSONObject(i);
                            int id = jsonCountry.getInt("id");
                            String name = jsonCountry.getString("name");
                            String code = jsonCountry.getString("code");

                            //add the products to the array
                            countries.add(new Country.Builder()
                                    .setId(id)
                                    .setCode(code)
                                    .setName(name)
                                    .build());
                        }

                        //We are using a callback because the return statement is executed before
                        //the fetch response is complete
                        callback.onSuccess(countries);

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

    public static void fetchStates(Activity context, StateCallback callback, String code) {


        final String fetchStatesUrl = Util.apiUrl + "/states/search/findStatesByCountry_Code?code=" + code;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                fetchStatesUrl,
                null,
                response -> {
                    try {

                        List<State> states = new ArrayList<>();

                        //We need to get the _embedded object first because the products array
                        //is nested within it
                        JSONObject embedded = response.getJSONObject("_embedded");

                        //Now, we can get the products array
                        JSONArray statesArray = embedded.getJSONArray("states");

                        for (int i = 0; i < statesArray.length(); i++) {
                            JSONObject jsonState = statesArray.getJSONObject(i);
                            int id = jsonState.getInt("id");
                            String name = jsonState.getString("name");

                            //add the products to the array
                            states.add(new State.Builder()
                                    .setId(id)
                                    .setName(name)
                                    .build());
                        }

                        //We are using a callback because the return statement is executed before
                        //the fetch response is complete
                        callback.onSuccess(states);

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
