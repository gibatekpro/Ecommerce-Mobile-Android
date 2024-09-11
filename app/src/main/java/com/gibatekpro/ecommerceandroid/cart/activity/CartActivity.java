package com.gibatekpro.ecommerceandroid.cart.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.gibatekpro.ecommerceandroid.cart.callback.CartItemClickCallback;
import com.gibatekpro.ecommerceandroid.cart.model.CartItem;
import com.gibatekpro.ecommerceandroid.cart.recyclerview.CartItemAdapter;
import com.gibatekpro.ecommerceandroid.checkout.activity.CheckoutActivity;
import com.gibatekpro.ecommerceandroid.databinding.ActivityCartBinding;
import com.gibatekpro.ecommerceandroid.util.Util;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemClickCallback {

    private static final String TAG = "CartActivity";

    private ActivityCartBinding binding;

    private CartViewModel cartViewModel;

    Activity activity = this;

    private Toolbar myToolbar;

    private CartItemAdapter adapter;

    private RecyclerView recyclerView;

    private Button checkOutButton;

    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartViewModel = new ViewModelProvider(this)
                .get(CartViewModel.class);

        myToolbar = binding.cartToolbar;
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = binding.recyclerview;

        checkOutButton = binding.checkOutButton;

        checkOutButton.setOnClickListener(v -> openCheckoutActivity());

        adapter = new CartItemAdapter(new CartItemAdapter.CartDiff(), this, this);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        loadCartData();

    }

    @Override
    public View.OnClickListener onAddClick(int position, CartItem cartItem) {

        int response = cartViewModel.addToCart(cartItems, cartItem);


        displayResponse(response, null);


        return null;
    }

    @Override
    public View.OnClickListener onMinusClick(int position, CartItem cartItem) {


        int response = cartViewModel.decrementQuantity(cartItems, cartItem);

        displayResponse(response, null);

        return null;
    }

    @Override
    public View.OnClickListener onDeleteClick(int position, CartItem cartItem) {

        int response = cartViewModel.deleteCartItem(cartItems, cartItem);

        displayResponse(response, "Item Deleted");

        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCartData() {

        cartViewModel.getCartItems().observe(this, cartItems1 -> {

            cartItems = cartItems1;

            if (cartItems == null || cartItems.isEmpty()) {
                checkOutButton.setVisibility(View.INVISIBLE);
                adapter.submitList(null);
            } else {
                adapter.submitList(cartItems);
            }

            int totalQuantity = cartViewModel.getCartTotal(cartItems1).getTotalQuantity();

            double totalPrice = cartViewModel.getCartTotal(cartItems1).getTotalPrice();

            checkOutButton.setText(String.format("Checkout " + "( " + "$" + totalPrice + " )"));

        });

    }

    private void displayResponse(int response, String message) {

        if (response > -1 && message != null) {
            Util.makeToast(this, message);
        } else if (response < 0) {
            Util.makeToast(this, "Error");
        }

    }

    private void openCheckoutActivity() {
        Intent n = new Intent(this, CheckoutActivity.class);

        startActivity(n);
    }


}