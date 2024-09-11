package com.gibatekpro.ecommerceandroid.product.callback;

import com.gibatekpro.ecommerceandroid.product.model.ProductCategory;

import java.util.List;

public interface ProductCategoryCallback {

    void onSuccess(List<ProductCategory> productCategories);

    void onFailure(String error);

}
