package com.gibatekpro.ecommerceandroid.product.service.products_category;

import android.app.Activity;

import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.gibatekpro.ecommerceandroid.product.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductByCategoryPagingSource extends RxPagingSource<Integer, Product> {

    private static final String TAG = "ProductByCategoryPaging";

    private final Activity context;

    private final int categoryId;

    public ProductByCategoryPagingSource(Activity context, int categoryId) {
        this.context = context;
        this.categoryId = categoryId;
    }

    @NotNull
    @Override
    public Single<LoadResult<Integer, Product>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        try {
            // If page number is already there, then initialize the page variable with it;
            // otherwise, we are loading the first page
            int page = loadParams.getKey() != null ? loadParams.getKey() : 0;

            // Send request to the server with the page number
            return ProductByCategoryApiClient.getAPIInterface(context, categoryId)
                    .getProductsByPage(categoryId, page)
                    .subscribeOn(Schedulers.io())
                    .map(productResponse -> {
                        List<Product> products = productResponse.get_embedded().getProducts();
//                        Log.d(TAG, "loadSingle: " + products);
                        return new LoadResult.Page<>(products, page == 0 ? null : page - 1, page + 1);
                    });
        } catch (Exception e) {
            // Request ran into an error, return error
//            Log.d(TAG, "loadSingle: " + e.getMessage());
            return Single.just(new LoadResult.Error(e));
        }
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NotNull PagingState<Integer, Product> pagingState) {
        return null;
    }

}