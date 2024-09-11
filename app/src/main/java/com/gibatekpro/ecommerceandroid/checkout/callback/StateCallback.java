package com.gibatekpro.ecommerceandroid.checkout.callback;

import com.gibatekpro.ecommerceandroid.checkout.model.State;

import java.util.List;

public interface StateCallback {

    void onSuccess(List<State> states);

    void onFailure(String error);

}
