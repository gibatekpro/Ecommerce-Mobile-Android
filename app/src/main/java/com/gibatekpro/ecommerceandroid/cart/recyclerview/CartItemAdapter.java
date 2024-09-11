package com.gibatekpro.ecommerceandroid.cart.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.gibatekpro.ecommerceandroid.cart.callback.CartItemClickCallback;
import com.gibatekpro.ecommerceandroid.cart.callback.CartItemsCallback;
import com.gibatekpro.ecommerceandroid.cart.model.CartItem;
import com.gibatekpro.ecommerceandroid.product.model.Product;

public class CartItemAdapter extends ListAdapter<CartItem, CartItemViewHolder> {

    final private Context context;

    final private CartItemClickCallback cartItemClickCallback;

    private static final String TAG = "CartItemAdapter";

    public CartItemAdapter(@NonNull DiffUtil.ItemCallback<CartItem> diffCallback, Context context, CartItemClickCallback cartItemClickCallback) {
        super(diffCallback);

        this.context = context;

        this.cartItemClickCallback = cartItemClickCallback;

    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CartItemViewHolder.create(parent, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {

        CartItem cartItem = getItem(position);
        holder.bind(cartItem);

        if (cartItemClickCallback != null) {

            holder.addButton.setOnClickListener(v -> cartItemClickCallback.onAddClick(position, cartItem));
            holder.deleteButton.setOnClickListener(v -> cartItemClickCallback.onDeleteClick(position, cartItem));
            holder.minusButton.setOnClickListener(v -> cartItemClickCallback.onMinusClick(position, cartItem));

        }

    }

    public static class CartDiff extends DiffUtil.ItemCallback<CartItem> {

        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return false;
        }
    }

}
