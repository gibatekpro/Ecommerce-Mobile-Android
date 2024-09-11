package com.gibatekpro.ecommerceandroid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.SocketTimeoutException;

public class NetworkCheck extends AppCompatActivity {

    public ConnectivityManager connectivityManager;
    public NetworkInfo networkInfo;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo = connectivityManager.getActiveNetworkInfo();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof SocketTimeoutException) {
                // Handle the SocketTimeoutException here
                Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show();
            } else {
                // Handle other uncaught exceptions
                Toast.makeText(this, "An error occurred: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
