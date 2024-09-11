package com.gibatekpro.ecommerceandroid.cart.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gibatekpro.ecommerceandroid.R;
import com.gibatekpro.ecommerceandroid.cart.model.CartItem;
import com.gibatekpro.ecommerceandroid.util.ImageLoader;

import java.math.BigDecimal;

public class CartItemViewHolder extends RecyclerView.ViewHolder {

    ImageView cartItemImage;
    ImageButton deleteButton, minusButton, addButton;
    TextView cartItemName, cartItemPrice, cartItemStock, cartItemSubtotal, cartItemQuantity;

    private final Context context;

    public CartItemViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        this.context = context;

        cartItemImage = itemView.findViewById(R.id.thumbnail);
        cartItemName = itemView.findViewById(R.id.cartItemName);
        cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
        cartItemStock = itemView.findViewById(R.id.cartItemStock);
        cartItemSubtotal = itemView.findViewById(R.id.cartItemSubtotal);
        cartItemQuantity = itemView.findViewById(R.id.cartItemQuantity);
        deleteButton = itemView.findViewById(R.id.deleteButton);
        minusButton = itemView.findViewById(R.id.minusButton);
        addButton = itemView.findViewById(R.id.addButton);

    }

    public void bind(CartItem cartItem) {

        BigDecimal itemPrice = cartItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        double subtotal = itemPrice.doubleValue();

        ImageLoader.loadImage(context, cartItemImage, cartItem.getImageUrl());
        cartItemName.setText(cartItem.getName());
        cartItemPrice.setText(String.format("Unit Price: " + "$" + cartItem.getUnitPrice().doubleValue()));
        cartItemStock.setText(String.format("Units in Stock: " + cartItem.getUnitsInStock()));
        cartItemSubtotal.setText(String.format("Subtotal: " + "$" + subtotal));
        cartItemQuantity.setText(String.valueOf(cartItem.getQuantity()));

    }

    public static CartItemViewHolder create(ViewGroup parent, Context context){

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_view_item, parent, false);

        return new CartItemViewHolder(view, context);
    }
}
