package com.gibatekpro.ecommerceandroid.checkout.callback;

import com.gibatekpro.ecommerceandroid.checkout.model.Country;

import java.util.List;

public interface CountryCallback {

    void onSuccess(List<Country> countries);

    void onFailure(String error);

}
