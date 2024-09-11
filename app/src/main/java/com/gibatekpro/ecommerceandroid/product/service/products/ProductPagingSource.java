package com.gibatekpro.ecommerceandroid.product.service.products;

import android.app.Activity;
import android.util.Log;

import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.gibatekpro.ecommerceandroid.product.callback.ProductCallback;
import com.gibatekpro.ecommerceandroid.product.model.Product;
import com.gibatekpro.ecommerceandroid.product.model.ProductResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPagingSource extends RxPagingSource<Integer, Product> {
    private static final String TAG = "ProductPagingSource";

    private Activity context;

    private ProductCallback callback;

    public ProductPagingSource(Activity context, ProductCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @NotNull
    @Override
    public Single<LoadResult<Integer, Product>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        try {
            // If page number is already there, then initialize the page variable with it;
            // otherwise, we are loading the first page
            int page = loadParams.getKey() != null ? loadParams.getKey() : 0;

            // Send request to the server with the page number
            return Single.create(emitter -> {
                Call<ProductResponse> call = ProductApiClient.getAPIInterface(context).getProductsByPage(page);
                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                        if (response.isSuccessful()) {

                            ProductResponse productResponse = response.body();

                            if (productResponse != null) {
                                List<Product> products = productResponse.get_embedded().getProducts();
                                emitter.onSuccess(new LoadResult.Page<>(products, page == 0 ? null : page - 1, page + 1));
                                callback.onSuccess(products);
                            }else {
                                emitter.onError(new Exception("Request failed with code: " + response.code()));
                            }

                        }else {
                            emitter.onError(new Exception("Empty response body"));
                        }

                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        callback.onFailure(t.toString());
                        emitter.onError(t);
                    }
                });
            });
        } catch (Exception e) {
            // Request ran into an error, return error
            Log.d(TAG, "loadSingle: ==>>" + e.getMessage());
            return Single.just(new LoadResult.Error(e));
        }
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NotNull PagingState<Integer, Product> pagingState) {
        return null;
    }

}