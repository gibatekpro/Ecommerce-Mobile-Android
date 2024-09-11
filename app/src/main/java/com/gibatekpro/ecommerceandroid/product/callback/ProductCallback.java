package com.gibatekpro.ecommerceandroid.product.callback;

import com.gibatekpro.ecommerceandroid.product.model.Product;

import java.util.List;

public interface ProductCallback {

    void onSuccess(List<Product> products);

    void onFailure(String error);

}
