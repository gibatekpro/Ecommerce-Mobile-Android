package com.gibatekpro.ecommerceandroid.checkout.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.gibatekpro.ecommerceandroid.R;
import com.gibatekpro.ecommerceandroid.cart.activity.CartViewModel;
import com.gibatekpro.ecommerceandroid.cart.model.CartItem;
import com.gibatekpro.ecommerceandroid.checkout.callback.PaymentIntentCallback;
import com.gibatekpro.ecommerceandroid.checkout.callback.PurchaseCallback;
import com.gibatekpro.ecommerceandroid.checkout.model.Address;
import com.gibatekpro.ecommerceandroid.checkout.model.Country;
import com.gibatekpro.ecommerceandroid.checkout.model.Customer;
import com.gibatekpro.ecommerceandroid.checkout.model.Order;
import com.gibatekpro.ecommerceandroid.checkout.model.OrderItem;
import com.gibatekpro.ecommerceandroid.checkout.model.OrderResponse;
import com.gibatekpro.ecommerceandroid.checkout.model.PaymentInfo;
import com.gibatekpro.ecommerceandroid.checkout.model.Purchase;
import com.gibatekpro.ecommerceandroid.checkout.model.State;
import com.gibatekpro.ecommerceandroid.checkout.service.CheckoutService;
import com.gibatekpro.ecommerceandroid.checkout.service.PaymentIntentService;
import com.gibatekpro.ecommerceandroid.checkout.util.CustomEditText;
import com.gibatekpro.ecommerceandroid.databinding.ActivityCheckoutBinding;
import com.gibatekpro.ecommerceandroid.util.Util;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = "CheckoutActivity";

    private ActivityCheckoutBinding binding;

    private CheckoutViewModel checkoutViewModel;

    private CartViewModel cartViewModel;

    private Button submitButton;

    private Customer customer;

    private Address shipAddress;

    private Address billAddress;

    private Order order;

    private List<OrderItem> orderItems;

    int totalQuantity = 0;

    double totalPrice = 0;

    private Purchase purchase;


    List<Country> shipCountryList = new ArrayList<>();

    ArrayAdapter<String> shipCountryAdapter;

    Country selectedShipCountry;

    State selectedShipState;

    List<State> shipStateList = new ArrayList<>();

    ArrayAdapter<String> shipStateAdapter;


    List<Country> billCountryList = new ArrayList<>();

    ArrayAdapter<String> billCountryAdapter;

    Country selectedBillCountry;

    State selectedBillState;

    List<State> billStateList = new ArrayList<>();

    ArrayAdapter<String> billStateAdapter;

    private boolean isLoading = false;


    private Toolbar myToolbar;

    CheckBox checkBox;

    boolean isChecked = false;

    private boolean billingIsShipping = false;

    private LinearLayout billLayout;

    private List<CartItem> cartItems;


    CustomEditText mFirstName;
    CustomEditText mLastName;
    CustomEditText mEmail;
    CustomEditText mShipStreet;
    CustomEditText mShipCity;
    CustomEditText mShipZipCode;
    CustomEditText mShipCountry;
    CustomEditText mShipState;

    CustomEditText mBillStreet;
    CustomEditText mBillCity;
    CustomEditText mBillZipCode;
    CustomEditText mBillCountry;
    CustomEditText mBillState;

    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;

    private String paymentClientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PaymentConfiguration.init(
                getApplicationContext(),
                Util.publishableKey
        );


        checkoutViewModel = new ViewModelProvider(this)
                .get(CheckoutViewModel.class);

        cartViewModel = new ViewModelProvider(this)
                .get(CartViewModel.class);

        personalData();

        shippingData();

        billingData();

        billLayout = binding.billLayout;

        checkBox = binding.copyAddressCheckBox;

        submitButton = binding.submitButton;


        myToolbar = binding.checkoutToolbar;
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        submitButton.setOnClickListener(v -> onSubmit());

        checkBox.setOnClickListener(v -> {

            isChecked = !isChecked;

            checkBox.setChecked(isChecked);

            if (isChecked) {
                billingIsShipping = true;
                billLayout.setVisibility(View.GONE);
            } else {
                billLayout.setVisibility(View.VISIBLE);
                billingIsShipping = false;
            }

        });

        loadCartData();

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {

        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "==>>: Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "==>>: Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {

            processCheckout();

        }
    }

    private void initPaymentIntent(PaymentInfo paymentInfo) {

        PaymentIntentService.createPaymentIntent(this, paymentInfo, new PaymentIntentCallback() {
            @Override
            public void onSuccess(JSONObject intentResponse) {
                try {

//                    customerConfig = new PaymentSheet.CustomerConfiguration(
//                            null,
//                            intentResponse.getString("ephemeralKey")
//                    );
                    paymentIntentClientSecret = intentResponse.getString("client_secret");
                    Log.d(TAG, "onSuccess: ==>>" + paymentIntentClientSecret);
                    Log.d(TAG, "onSuccess: ==>>" + Util.publishableKey);
                    PaymentConfiguration.init(getApplicationContext(), Util.publishableKey);

                    presentPaymentSheet();


                } catch (JSONException e) {
                    /* handle error */
                    Log.d(TAG, "failure: ==>> Try" + e.toString());
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "failure: ==>> OnFailure" + error.toString());
            }
        });

    }

    private void presentPaymentSheet() {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Example, Inc.")
                .allowsDelayedPaymentMethods(true)
                .build();
        if (paymentIntentClientSecret != null) {
            paymentSheet.presentWithPaymentIntent(
                    paymentIntentClientSecret,
                    configuration
            );
        } else {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCartData() {

        cartViewModel.getCartItems().observe(this, cartItems -> {

            this.cartItems = cartItems;

            totalQuantity = cartViewModel.getCartTotal(cartItems).getTotalQuantity();

            totalPrice = cartViewModel.getCartTotal(cartItems).getTotalPrice();

            orderItems = new ArrayList<>();

            for (CartItem cartItem : cartItems) {

                orderItems.add(new OrderItem.Builder()
                        .setUnitPrice(cartItem.getUnitPrice().doubleValue())
                        .setImageUrl(cartItem.getImageUrl())
                        .setQuantity(cartItem.getQuantity())
                        .setProductId((int) cartItem.getId())
                        .build());

            }


        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void onSubmit() {

        boolean formIsValid = checkFormValidation();


        if (formIsValid) {

            customer = new Customer.Builder()
                    .setEmail(mEmail.getText())
                    .setLastName(mLastName.getText())
                    .setFirstName(mFirstName.getText())
                    .build();

            shipAddress = new Address.Builder()
                    .setStreet(mShipStreet.getText())
                    .setCity(mShipCity.getText())
                    .setCountry(mShipCountry.getText())
                    .setState(mShipState.getText())
                    .setZipCode(mShipZipCode.getText())
                    .build();

            if (billingIsShipping) {
                billAddress = new Address.Builder()
                        .setStreet(mShipStreet.getText())
                        .setCity(mShipCity.getText())
                        .setCountry(mShipCountry.getText())
                        .setState(mShipState.getText())
                        .setZipCode(mShipZipCode.getText())
                        .build();
            } else {

                billAddress = new Address.Builder()
                        .setStreet(mBillStreet.getText())
                        .setCity(mBillCity.getText())
                        .setCountry(mBillCountry.getText())
                        .setState(mBillState.getText())
                        .setZipCode(mBillZipCode.getText())
                        .build();
            }

            order = new Order.Builder()
                    .setTotalPrice(totalPrice)
                    .setTotalQuantity(totalQuantity)
                    .build();

            purchase = new Purchase.Builder()
                    .setCustomer(customer)
                    .setShippingAddress(shipAddress)
                    .setBillingAddress(billAddress)
                    .setOrder(order)
                    .setOrderItems(orderItems)
                    .build();

            PaymentInfo paymentInfo = new PaymentInfo.Builder()
                    .amount((int) (order.getTotalPrice() * 100))
                    .currency("USD")
                    .description("Ecommerce Shop")
                    .receiptEmail(purchase.getCustomer().getEmail())
                    .build();


            initPaymentIntent(paymentInfo);


        }


    }

    private void processCheckout() {
        CheckoutService.placeOrder(this, purchase, new PurchaseCallback() {
            @Override
            public void onSuccess(OrderResponse orderResponse) {

                Util.hideKeyBoard(CheckoutActivity.this);

                Util.showLongSnack(CheckoutActivity.this, "Successful!!" + "\n" + "Order Tracking Number: " + orderResponse.getOrderTrackingNumber());

//                Toast.makeText(CheckoutActivity.this, "Successful: " + orderResponse.getOrderTrackingNumber().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(String error) {

                Toast.makeText(CheckoutActivity.this, "Failed: " + error, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean checkFormValidation() {

        if (isChecked) {

            if (!mFirstName.isValid()) {
                return false;
            }
            if (!mLastName.isValid()) {
                return false;
            }
            if (!mEmail.isValid()) {
                return false;
            }
            if (!mShipStreet.isValid()) {
                return false;
            }
            if (!mShipCity.isValid()) {
                return false;
            }
            if (!mShipCountry.isValid()) {
                return false;
            }
            if (!mShipState.isValid()) {
                return false;
            }

        } else {

            if (!mFirstName.isValid()) {
                return false;
            }
            if (!mLastName.isValid()) {
                return false;
            }
            if (!mEmail.isValid()) {
                return false;
            }
            if (!mShipStreet.isValid()) {
                return false;
            }
            if (!mShipCity.isValid()) {
                return false;
            }
            if (!mShipCountry.isValid()) {
                return false;
            }
            if (!mShipState.isValid()) {
                return false;
            }

            if (!mBillStreet.isValid()) {
                return false;
            }
            if (!mBillCity.isValid()) {
                return false;
            }
            if (!mBillCountry.isValid()) {
                return false;
            }
            if (!mBillState.isValid()) {
                return false;
            }
        }
        return true;
    }

    private void personalData() {
        EditText firstName;
        EditText lastName;
        EditText email;

        firstName = binding.firstNameTextField;
        lastName = binding.lastNameTextField;
        email = binding.emailTextField;

        mFirstName = new CustomEditText(3, firstName, false);
        mLastName = new CustomEditText(3, lastName, false);
        mEmail = new CustomEditText(3, email, true);

    }

    private void shippingData() {

        EditText street;

        EditText city;

        EditText zipCode;

        street = binding.shipStreetField;
        city = binding.shipCityField;
        zipCode = binding.shipZipCodeField;

        List<String> countryNames = new ArrayList<>();

        AutoCompleteTextView shipCountryField;

        shipCountryField = binding.shipCountry;

        List<String> stateNames = new ArrayList<>();

        AutoCompleteTextView shipStateField;

        shipStateField = binding.shipState;

        checkoutViewModel.getCountries(this).observe(this, countries -> {

            if (countries != null) {

                shipCountryList = countries;

                countryNames.clear();

                for (Country country : countries) {

                    countryNames.add(country.getName());

                }

                shipCountryAdapter = new ArrayAdapter<>(CheckoutActivity.this, R.layout.dropdown_textview, R.id.shipCountryMenu, countryNames);

                shipCountryField.setAdapter(shipCountryAdapter);

            }

        });

        shipCountryField.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);

            shipStateList.clear();

            shipStateField.setText("");

            for (Country country : shipCountryList) {
                if (country.getName().equals(selectedItem)) {
                    selectedShipCountry = country;
                    break;
                }
            }

            Toast.makeText(this, selectedShipCountry.getCode(), Toast.LENGTH_SHORT).show();

            checkoutViewModel.getStates(this, selectedShipCountry.getCode()).observe(this, states -> {
                if (states != null) {

                    shipStateList = states;

//                    Toast.makeText(this, states.toString(), Toast.LENGTH_SHORT).show();

                    stateNames.clear();

                    for (State state : states) {

                        stateNames.add(state.getName());

                    }

                    shipStateAdapter = new ArrayAdapter<>(CheckoutActivity.this, R.layout.dropdown_textview, R.id.shipStateMenu, stateNames);

                    shipStateField.setAdapter(shipStateAdapter);
                }
            });
        });

        shipStateField.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            for (State state : shipStateList) {
                if (state.getName().equals(selectedItem)) {
                    selectedShipState = state;
                    break;
                }
            }
        });

        mShipStreet = new CustomEditText(3, street, false);
        mShipCity = new CustomEditText(3, city, false);
        mShipZipCode = new CustomEditText(3, zipCode, false);
        mShipCountry = new CustomEditText(3, shipCountryField, false);
        mShipState = new CustomEditText(3, shipStateField, false);


    }

    private void billingData() {

        EditText street;

        EditText city;

        EditText zipCode;

        street = binding.billStreetField;
        city = binding.billCityField;
        zipCode = binding.billZipCodeField;


        List<String> countryNames = new ArrayList<>();

        AutoCompleteTextView billCountryField;

        billCountryField = binding.billCountry;

        List<String> stateNames = new ArrayList<>();

        AutoCompleteTextView billStateField;

        billStateField = binding.billState;

        checkoutViewModel.getCountries(this).observe(this, countries -> {

            if (countries != null) {

                billCountryList = countries;

                countryNames.clear();

                for (Country country : countries) {

                    countryNames.add(country.getName());

                }

                billCountryAdapter = new ArrayAdapter<>(CheckoutActivity.this, R.layout.dropdown_textview, R.id.billCountryMenu, countryNames);

                billCountryField.setAdapter(billCountryAdapter);

            }

        });

        billCountryField.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);

            billStateList.clear();

            billStateField.setText("");

            for (Country country : billCountryList) {
                if (country.getName().equals(selectedItem)) {
                    selectedBillCountry = country;
                    break;
                }
            }

            Toast.makeText(this, selectedBillCountry.getCode(), Toast.LENGTH_SHORT).show();

            checkoutViewModel.getStates(this, selectedBillCountry.getCode()).observe(this, states -> {
                if (states != null) {

                    billStateList = states;

//                    Toast.makeText(this, states.toString(), Toast.LENGTH_SHORT).show();

                    stateNames.clear();

                    for (State state : states) {

                        stateNames.add(state.getName());

                    }

                    billStateAdapter = new ArrayAdapter<>(CheckoutActivity.this, R.layout.dropdown_textview, R.id.billStateMenu, stateNames);

                    billStateField.setAdapter(billStateAdapter);
                }
            });
        });

        billStateField.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            for (State state : billStateList) {
                if (state.getName().equals(selectedItem)) {
                    selectedBillState = state;
                    break;
                }
            }
        });

        mBillStreet = new CustomEditText(3, street, false);
        mBillCity = new CustomEditText(3, city, false);
        mBillZipCode = new CustomEditText(3, zipCode, false);
        mBillCountry = new CustomEditText(3, billCountryField, false);
        mBillState = new CustomEditText(3, billStateField, false);


    }


//            Fuel.INSTANCE.post(Util.apiUrl + "/checkout/payment-intent",paymentInfo).responseString(new Handler<String>() {
//        @Override
//        public void success(String s) {
//            try {
//                final JSONObject result = new JSONObject(s);
//                customerConfig = new PaymentSheet.CustomerConfiguration(
//                        result.getString("customer"),
//                        result.getString("ephemeralKey")
//                );
//                paymentIntentClientSecret = result.getString("paymentIntent");
//                PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));
//            } catch (JSONException e) {
//                /* handle error */
//                Log.d(TAG, "failure: ==>> " + e.toString());
//            }
//        }
//
//        @Override
//        public void failure(@NonNull FuelError fuelError) {
//            Log.d(TAG, "failure: ==>> " + fuelError.toString());
//        }
//    });


}