package com.gibatekpro.ecommerceandroid.product.activity.product_details;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gibatekpro.ecommerceandroid.R;
import com.gibatekpro.ecommerceandroid.cart.activity.CartActivity;
import com.gibatekpro.ecommerceandroid.cart.activity.CartViewModel;
import com.gibatekpro.ecommerceandroid.cart.model.CartItem;
import com.gibatekpro.ecommerceandroid.product.activity.products.ProductActivity;
import com.gibatekpro.ecommerceandroid.databinding.ActivityProductDetailsBinding;
import com.gibatekpro.ecommerceandroid.product.model.Product;
import com.gibatekpro.ecommerceandroid.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailsActivity";

    private Toolbar myToolbar;

    private CartViewModel cartViewModel;

    ImageView productImage;
    TextView productName, productDescription, productUnitPrice, productUnitsInStock, productDateCreated;

    Button addToCart, openCart;

    private ActivityProductDetailsBinding productBinding;

    private List<CartItem> cartItems = new ArrayList<>();

    Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productBinding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(productBinding.getRoot());

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        myToolbar = productBinding.detailsToolbar;
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra(ProductActivity.KEY_PRODUCT_DETAILS);

        productImage = productBinding.productDetailsImage;
        productName = productBinding.productDetailsName;
        productDescription = productBinding.productDetailsDesc;
        productUnitPrice = productBinding.productDetailsPrice;
        productUnitsInStock = productBinding.productDetailsUnits;
        productDateCreated = productBinding.productDetailsUnits;
        addToCart = productBinding.productDetailsAddToCart;
        openCart = productBinding.openCart;

        ImageLoader.loadImage(this, productImage, product.getImageUrl());
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productUnitPrice.setText(String.format(getString(R.string.unit_price) + getString(R.string.dollar_sign) + product.getUnitPrice().toString()));
        productUnitsInStock.setText(String.format(getString(R.string.unit_in_stock) + product.getUnitsInStock()));
        productDateCreated.setText(String.format("Date Created: " + product.getDateCreated()));

        addToCart.setOnClickListener(v -> cartViewModel.addToCart(cartItems, new CartItem(product)));

        openCart.setOnClickListener(v -> {

            openCartActivity();

        });


    }

    private void openCartActivity() {
        Intent n = new Intent(this, CartActivity.class);

        startActivity(n);
    }

    //https://developer.android.com/develop/ui/views/search/search-dialog
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_details_menu, menu);

        MenuItem cartItem = menu.findItem(R.id.action_cart_product_details);

        RelativeLayout rootView = (RelativeLayout) cartItem.getActionView();
        LayoutInflater inflaterB = LayoutInflater.from(this);
        View badgeLayout = inflaterB.inflate(R.layout.cart_badge, rootView, false);
        rootView.addView(badgeLayout);

        TextView badgeTextView = badgeLayout.findViewById(R.id.the_cart_badge);

        loadCartData(badgeTextView);

        rootView.setOnClickListener(v -> openCartActivity());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCartData(TextView badgeTextView) {

        cartViewModel.getCartItems().observe(this, cartItems1 -> {

            cartItems = cartItems1;

            int totalQuantity = cartViewModel.getCartTotal(cartItems1).getTotalQuantity();
            try {
                badgeTextView.setText(String.valueOf(totalQuantity));
            } catch (Exception e) {
                Log.e(TAG, "loadItems: ==>> " + e.getMessage(), e);
            }


        });

    }

}