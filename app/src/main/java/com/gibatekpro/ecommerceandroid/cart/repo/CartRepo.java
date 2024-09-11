package com.gibatekpro.ecommerceandroid.cart.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.gibatekpro.ecommerceandroid.cart.dao.CartDao;
import com.gibatekpro.ecommerceandroid.cart.database.CartDatabase;
import com.gibatekpro.ecommerceandroid.cart.model.CartItem;
import com.gibatekpro.ecommerceandroid.cart.model.CartTotal;

import java.math.BigDecimal;
import java.util.List;

public class CartRepo {

    private static final String TAG = "CartRepo";

    private final CartDao cartDao;

    CartDatabase db;

    Application application;

    LiveData<List<CartItem>> cartItems;

    CartItem existingCartItem = new CartItem();

    int response;

    public CartRepo(Application application) {

        this.application = application;

        // Initialize database
        db = CartDatabase.getDatabase(application);
        cartDao = db.cartDao();

        cartItems = cartDao.getAllCartItems();

    }

    public LiveData<List<CartItem>> getAllCartItems() {

        return cartItems;

    }

    public CartTotal getCartTotal(List<CartItem> cartItems1) {
        double totalPriceValue = 0;
        int totalQuantityValue = 0;

        if (cartItems1 != null) {
            for (CartItem currentCartItem : cartItems1) {
                BigDecimal itemPrice = currentCartItem.getUnitPrice()
                        .multiply(BigDecimal.valueOf(currentCartItem.getQuantity()));
                totalPriceValue = totalPriceValue + itemPrice.doubleValue();
                totalQuantityValue += currentCartItem.getQuantity();
            }
        }

        return new CartTotal(totalPriceValue, totalQuantityValue);

    }

    public int addToCart(List<CartItem> cartItems, CartItem theCartItem) {

        //Set the alreadyExisting to false so that we can check if this
        //item exists in the cart
        boolean alreadyExistsInCart = false;

        if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItem cartItem : cartItems) {
                if (cartItem.getId() == theCartItem.getId()) {
                    existingCartItem = cartItem;
                    alreadyExistsInCart = true;
                    break;
                }
            }
        }


        if (alreadyExistsInCart) {
            existingCartItem.incrementQuantity();
            //Run on separate thread
            CartDatabase.databaseWriteExecutor.execute(() -> {
                response = cartDao.updateCartItem(existingCartItem);// Update database
            });

        } else {
            //Run on separate thread
            CartDatabase.databaseWriteExecutor.execute(() -> {
                response = (int) cartDao.insert(theCartItem);
            });
        }

        return response;

    }

    public int decrementQuantity(List<CartItem> cartItems, CartItem cartItem) {

        cartItem.decrementQuantity();

        if (cartItem.getQuantity() == 0) {
            response = deleteCartItem(cartItems, cartItem);
        } else
            //Run on separate thread
            CartDatabase.databaseWriteExecutor.execute(() -> {
                response = cartDao.updateCartItem(cartItem);// Update database
            });

        return response;
    }

    public int deleteCartItem(List<CartItem> cartItems, CartItem theCartItem) {
        int itemIndex = -1;

        if (cartItems != null) {
            for (int i = 0; i < cartItems.size(); i++) {
                CartItem cartItem = cartItems.get(i);
                if (cartItem.getId() == theCartItem.getId()) {
                    itemIndex = i;
                    break;
                }
            }
        }

        if (itemIndex > -1) {
            CartDatabase.databaseWriteExecutor.execute(() -> {
                response = cartDao.deleteCartItem(theCartItem.getId());
            });
        }

        //reset itemIndex
        itemIndex = -1;

        return response;
    }

}
