package com.gibatekpro.ecommerceandroid.cart.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gibatekpro.ecommerceandroid.cart.model.CartItem;

import java.util.List;

@Dao
public interface CartDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(CartItem cartItem);

    @Update
    int updateCartItem(CartItem cartItem);

    @Query("DELETE FROM cart_table WHERE id = :itemId")
    int deleteCartItem(long itemId);

    @Query("DELETE FROM cart_table")
    int deleteAll();

    @Query("SELECT * FROM cart_table ORDER BY id ASC")
    LiveData<List<CartItem>> getAllCartItems();

}
