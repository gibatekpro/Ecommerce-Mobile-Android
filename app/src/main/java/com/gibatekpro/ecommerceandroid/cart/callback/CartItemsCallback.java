package com.gibatekpro.ecommerceandroid.cart.callback;

import com.gibatekpro.ecommerceandroid.cart.model.CartItem;

import java.util.List;

public interface CartItemsCallback {

    void getAllCartItems(List<CartItem> items);

}
