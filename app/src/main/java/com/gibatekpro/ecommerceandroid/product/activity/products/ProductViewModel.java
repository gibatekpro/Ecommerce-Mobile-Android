package com.gibatekpro.ecommerceandroid.product.activity.products;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.gibatekpro.ecommerceandroid.product.callback.ProductCallback;
import com.gibatekpro.ecommerceandroid.product.model.Product;
import com.gibatekpro.ecommerceandroid.product.service.products.ProductPagingSource;
import com.gibatekpro.ecommerceandroid.product.service.products_category.ProductByCategoryPagingSource;
import com.gibatekpro.ecommerceandroid.product.service.products_keyword.ProductByKeyWordPagingSource;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class ProductViewModel extends ViewModel {

    private static final String TAG = "ProductViewModel";

    //Paging source for products by category
    ProductByCategoryPagingSource productByCategoryPagingSource;

    //Paging source for all products
    ProductPagingSource productPagingSource;

    //Paging source for search
    ProductByKeyWordPagingSource productSearchPagingSource;

    //Flowable is like observable. It can be subscribed to
    Flowable<PagingData<Product>> productPagingDataFlowable;


    public ProductViewModel() {

    }

    public Flowable<PagingData<Product>> productPagingDataFlowable(Activity context, boolean isSearch, ProductCallback callback) {
        // Define Paging Source

        if (isSearch) {

            ProductActivity.getSearchKeyWord(
                    keyWord -> productSearchPagingSource
                            = new ProductByKeyWordPagingSource(context, keyWord));
            // Create new Pager
            Pager<Integer, Product> pager;
            pager = new Pager(
                    // Create new paging config
                    new PagingConfig(5, //  Count of items in one page
                            20, //  Number of items to prefetch
                            false, // Enable placeholders for data which is not yet loaded
                            20, // initialLoadSize - Count of items to be loaded initially
                            20 * 499),// maxSize - Count of total items to be shown in recyclerview
                    () -> productSearchPagingSource); // set paging source
            // init Flowable
            productPagingDataFlowable = PagingRx.getFlowable(pager);
            CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
            PagingRx.cachedIn(productPagingDataFlowable, coroutineScope);

            return productPagingDataFlowable;
        }

        AtomicInteger menuId = new AtomicInteger();

        ProductActivity.getMenuId(id -> {
            if (id == 0) {

                productPagingSource = new ProductPagingSource(context, callback);

                menuId.set(0);

            } else {

                productByCategoryPagingSource = new ProductByCategoryPagingSource(context, id);

                menuId.set(id);

                Log.d(TAG, "productPagingDataFlowable: " + id);
            }
        });


        if (menuId.get() == 0) {

            // Create new Pager
            Pager<Integer, Product> pager;
            pager = new Pager(
                    // Create new paging config
                    new PagingConfig(5, //  Count of items in one page
                            20, //  Number of items to prefetch
                            false, // Enable placeholders for data which is not yet loaded
                            20, // initialLoadSize - Count of items to be loaded initially
                            20 * 499),// maxSize - Count of total items to be shown in recyclerview
                    () -> productPagingSource); // set paging source
            // inti Flowable
            productPagingDataFlowable = PagingRx.getFlowable(pager);
            CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
            PagingRx.cachedIn(productPagingDataFlowable, coroutineScope);


        } else {
            // Create new Pager
            Pager<Integer, Product> pager;
            pager = new Pager(
                    // Create new paging config
                    new PagingConfig(5, //  Count of items in one page
                            20, //  Number of items to prefetch
                            false, // Enable placeholders for data which is not yet loaded
                            20, // initialLoadSize - Count of items to be loaded initially
                            20 * 499),// maxSize - Count of total items to be shown in recyclerview
                    () -> productByCategoryPagingSource); // set paging source
            // init Flowable
            productPagingDataFlowable = PagingRx.getFlowable(pager);
            CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
            PagingRx.cachedIn(productPagingDataFlowable, coroutineScope);

        }

        return productPagingDataFlowable;

    }


}