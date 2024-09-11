package com.gibatekpro.ecommerceandroid.product.activity.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.gibatekpro.ecommerceandroid.R;
import com.gibatekpro.ecommerceandroid.cart.activity.CartActivity;
import com.gibatekpro.ecommerceandroid.cart.activity.CartViewModel;
import com.gibatekpro.ecommerceandroid.cart.model.CartItem;
import com.gibatekpro.ecommerceandroid.product.activity.product_details.ProductDetailsActivity;
import com.gibatekpro.ecommerceandroid.product.callback.FragmentCallback;
import com.gibatekpro.ecommerceandroid.product.callback.ProductCallback;
import com.gibatekpro.ecommerceandroid.product.callback.ProductCategoryCallback;
import com.gibatekpro.ecommerceandroid.product.callback.RecyclerItemClickListener;
import com.gibatekpro.ecommerceandroid.product.callback.SearchCallback;
import com.gibatekpro.ecommerceandroid.product.model.Product;
import com.gibatekpro.ecommerceandroid.product.model.ProductCategory;
import com.gibatekpro.ecommerceandroid.product.service.ProductCategoryService;
import com.gibatekpro.ecommerceandroid.product.recyclerview.ProductAdapter;
import com.gibatekpro.ecommerceandroid.util.HttpsTrustManager;
import com.gibatekpro.ecommerceandroid.util.NetworkCheck;
import com.gibatekpro.ecommerceandroid.util.Util;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductActivity extends NetworkCheck implements RecyclerItemClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Toolbar myToolbar;

    private static final String TAG = "ProductActivity";

    public static final String KEY_PRODUCT_DETAILS = "PRODUCT_DETAILS";

    public static final String categoryIdKey = "categoryId";

    private static int categoryID;

    private ProductViewModel productViewModel;

    private ProductAdapter adapter;

    private NavigationView navigationView;

    private Activity activity = this;

    private LifecycleOwner lifecycleOwner = this;

    private static String theSearchWord = "";

    private List<CartItem> cartItems = new ArrayList<>();

    private CartViewModel cartViewModel;

    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        categoryID = 0;

        // It is important to note that the localhost was self-signed,
        //and so, the android app cannot access the api naturally.
        // We had to use the HttpsManager found in util
        HttpsTrustManager.allowAllSSL();

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        myToolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(myToolbar);


        // We will create a custom menu for the navigation controller
        // This will be populated with the Product Categories
        createCustomDrawerMenu(navigationView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        adapter = new ProductAdapter(new ProductAdapter.ProductDiff(), this, this);

        recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        // set Grid span to set progress at center
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // If progress will be shown then span size will be 1 otherwise it will be 2
                return adapter.getItemViewType(position) == ProductAdapter.LOADING_ITEM ? 1 : 2;
            }
        });


        //If you set your listener before calling NavigationUI.setupWithNavController(navigationView, navController),
        // it will be overridden by the default listener and your code will not be executed.
        navigationView.setNavigationItemSelectedListener(item -> {

            categoryID = item.getItemId();

            loadItems(this, adapter, false);

            //Close the drawer after clicking on an item
            drawerLayout.close();
            return true;
        });

        loadItems(this, adapter, false);

    }

    //https://developer.android.com/develop/ui/views/search/search-dialog
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        MenuItem cartItem = menu.findItem(R.id.action_cart);

        RelativeLayout rootView = (RelativeLayout) cartItem.getActionView();
        LayoutInflater inflaterB = LayoutInflater.from(this);
        View badgeLayout = inflaterB.inflate(R.layout.cart_badge, rootView, false);
        rootView.addView(badgeLayout);

        TextView badgeTextView = badgeLayout.findViewById(R.id.the_cart_badge);

        loadCartData(badgeTextView);

        rootView.setOnClickListener(v -> openCartActivity());

        SearchView searchView =
                (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                theSearchWord = query;

                loadItems(activity, adapter, true);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                theSearchWord = newText;

                loadItems(activity, adapter, true);

                return false;
            }
        });

        // Customize the search view appearance
        searchView.setOnSearchClickListener(v -> {
            // Modify the Toolbar or other views when search view is expanded
            // Here you can update the Toolbar's title, hide other views, etc.
            myToolbar.setPopupTheme(R.style.SearchViewPopupTheme);
        });


        return true;
    }

    private void openCartActivity() {
        Intent n = new Intent(this, CartActivity.class);

        startActivity(n);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createCustomDrawerMenu(NavigationView navigationView) {

        Menu menu = navigationView.getMenu();

        menu.add(0, 0, 0, "All");

        //Populate the navigation drawer menu with Product Categories
        ProductCategoryService.fetchProductCategories(this, new ProductCategoryCallback() {
            @Override
            public void onSuccess(List<ProductCategory> productCategories) {

                for (ProductCategory category : productCategories) {
                    //We will use the Category ID as the menuItem ID, so if categories change, the item
                    //ID will also change
                    menu.add(0, Math.toIntExact(category.getId()), 0, category.getCategoryName());

                }

            }

            @Override
            public void onFailure(String error) {

                menu.add(0, 0, 0, "Error: " + error).setEnabled(false);

            }
        });

    }

    public static void getMenuId(FragmentCallback callback) {

        callback.menuId(categoryID);

    }

    public static void getSearchKeyWord(SearchCallback searchCallback) {

        searchCallback.searchKeyWord(theSearchWord);

    }

    @SuppressLint("CheckResult")
    public void loadItems(Activity context, ProductAdapter adapter, boolean isSearch) {

        if (networkInfo != null && networkInfo.isConnected()) {
            // Make your network request
            // Implement the logic to refresh the fragment here
            // productViewModel.getProducts(context).observe(this, adapter::submitList);
            productViewModel.productPagingDataFlowable(this, isSearch, new ProductCallback() {
                @Override
                public void onSuccess(List<Product> products) {

                }

                @Override
                public void onFailure(String error) {

                    Log.d(TAG, "onFailure: ==>> AAA" + error);

                }
            }).subscribe(productPagingData -> {
                // submit new data to recyclerview adapter
                adapter.submitData(getLifecycle(), productPagingData);
            }, throwable -> {
                Log.d(TAG, "loadItems: ");
            });

        } else {
            // Handle no network connection scenario
            Toast.makeText(this, "Connection error!!!", Toast.LENGTH_LONG).show();
        }

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

    @Override
    public View.OnClickListener onCardClick(int position, Product product) {

        //Make sure Product implements Serializable, or the code won't work
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(KEY_PRODUCT_DETAILS, product);
        startActivity(intent);

        return null;
    }

    @Override
    public View.OnClickListener onButtonClick(int position, Product product) {

        int response = cartViewModel.addToCart(cartItems, new CartItem(product));

        Log.d(TAG, "onButtonClick: ==>> " + cartItems.toString());

        if (response > -1) {
            Util.makeToast(this, String.valueOf(response));
        } else {
            Util.makeToast(this, "Error " + response);
        }

        return null;
    }

}