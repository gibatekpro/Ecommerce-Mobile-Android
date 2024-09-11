package com.gibatekpro.ecommerceandroid.product.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gibatekpro.ecommerceandroid.R;
import com.gibatekpro.ecommerceandroid.product.model.Product;
import com.gibatekpro.ecommerceandroid.util.ImageLoader;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    private final TextView nameView;
    private final TextView unitPriceView;
    private final ImageView thumbnailView;

    public final Button addToCart;

    public final CardView productCard;

    private final Context context;


    public ProductViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        this.context = context;

        nameView = itemView.findViewById(R.id.name);
        unitPriceView = itemView.findViewById(R.id.unitPrice);
        thumbnailView = itemView.findViewById(R.id.thumbnail);
        productCard = itemView.findViewById(R.id.productCard);
        addToCart = itemView.findViewById(R.id.addItem);

    }

    public void bind(Product product) {

        nameView.setText(product.getName());
        unitPriceView.setText(String.format("$" + product.getUnitPrice()));
        ImageLoader.loadImage(context, thumbnailView, product.getImageUrl());

    }


    static ProductViewHolder create(ViewGroup parent, Context context) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_view_item, parent, false);

        return new ProductViewHolder(view, context);
    }


}
