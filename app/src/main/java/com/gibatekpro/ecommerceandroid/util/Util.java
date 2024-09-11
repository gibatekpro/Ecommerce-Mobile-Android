package com.gibatekpro.ecommerceandroid.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gibatekpro.ecommerceandroid.checkout.activity.CheckoutActivity;
import com.google.android.material.snackbar.Snackbar;

public class Util {

    public static String baseUrl = "https://192.168.8.112:4040";

//    public static String apiUrl = "https://192.168.8.112:4040/api";

    public static String apiUrl = "https://192.168.8.112:4040/api";

    public static String publishableKey = "pk_test_51N5XrvG4mCGEpPxu7qpEIfRNYKHwzzoA2ZmAYQeYX3evd6qXwFPzK8OT5f4kspvLyfw91k737pZ62JMFHrmjUYRF00wwqOxI6R";

    public static void makeToast(Activity activity, String query) {
        Toast.makeText(activity, query, Toast.LENGTH_SHORT).show();
    }

    public static void showLongSnack(Activity activity, String message) {
        Snackbar snackbar =
                Snackbar
                        .make(
                                activity,
                                activity.getCurrentFocus().getRootView(),
                                message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public static void showShortSnack(Activity activity, String message, boolean hasAction) {
        Snackbar snackbar = Snackbar.make(activity, activity.getCurrentFocus().getRootView(), message, Snackbar.LENGTH_SHORT);
            snackbar.show();
    }

    public static void hideKeyBoard(Activity activity){
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}
