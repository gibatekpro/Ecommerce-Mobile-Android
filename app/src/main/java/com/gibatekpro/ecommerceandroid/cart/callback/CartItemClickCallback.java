package com.gibatekpro.ecommerceandroid.cart.callback;

import android.view.View;

import com.gibatekpro.ecommerceandroid.cart.model.CartItem;
import com.gibatekpro.ecommerceandroid.product.model.Product;

public interface CartItemClickCallback {

    View.OnClickListener onAddClick(int position, CartItem cartItem);

    View.OnClickListener onMinusClick(int position, CartItem cartItem);

    View.OnClickListener onDeleteClick(int position, CartItem cartItem);

}
