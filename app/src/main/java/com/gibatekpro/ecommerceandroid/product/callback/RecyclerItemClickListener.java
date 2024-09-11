package com.gibatekpro.ecommerceandroid.product.callback;

import android.view.View;

import com.gibatekpro.ecommerceandroid.product.model.Product;

public interface RecyclerItemClickListener {
    View.OnClickListener onCardClick(int position, Product product);

    View.OnClickListener onButtonClick(int position, Product product);

}