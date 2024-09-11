package com.gibatekpro.ecommerceandroid.product.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.gibatekpro.ecommerceandroid.product.callback.RecyclerItemClickListener;
import com.gibatekpro.ecommerceandroid.product.model.Product;

public class ProductAdapter extends PagingDataAdapter<Product, ProductViewHolder> {

    // Define Loading ViewType
    public static final int LOADING_ITEM = 0;
    // Define Movie ViewType
    public static final int PRODUCT_ITEM = 1;
//    RequestManager glide;

    public RecyclerItemClickListener recyclerItemClickListener;

    final private Context context;
    public ProductAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback, Context context, RecyclerItemClickListener recyclerItemClickListener) {
        super(diffCallback);
        this.context = context;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ProductViewHolder.create(parent, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = getItem(position);
        holder.bind(product);

        if (recyclerItemClickListener !=null) {
            holder.productCard.setOnClickListener(v -> recyclerItemClickListener.onCardClick(position, product));
            holder.addToCart.setOnClickListener(v -> recyclerItemClickListener.onButtonClick(position, product));
        }

    }


    public static class ProductDiff extends DiffUtil.ItemCallback<Product> {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return false;
        }
    }

}
