package com.gibatekpro.ecommerceandroid.cart.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gibatekpro.ecommerceandroid.cart.model.CartItem;
import com.gibatekpro.ecommerceandroid.cart.model.CartTotal;
import com.gibatekpro.ecommerceandroid.cart.repo.CartRepo;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    final private CartRepo cartRepo;

    final private LiveData<List<CartItem>> cartItems;


    public CartViewModel(@NonNull Application application) {
        super(application);

        cartRepo = new CartRepo(application);

        cartItems = cartRepo.getAllCartItems();

    }

    public LiveData<List<CartItem>> getCartItems(){
        return cartItems;
    }

    public CartTotal getCartTotal(List<CartItem> cartItems) {
        return cartRepo.getCartTotal(cartItems);
    }

    public int addToCart(List<CartItem> cartItems, CartItem cartItem) {
        return cartRepo.addToCart(cartItems, cartItem);
    }

    public int decrementQuantity(List<CartItem> cartItems, CartItem cartItem) {
        return cartRepo.decrementQuantity(cartItems, cartItem);
    }

    public int deleteCartItem(List<CartItem> cartItems, CartItem cartItem) {
        return cartRepo.deleteCartItem(cartItems, cartItem);
    }
}
