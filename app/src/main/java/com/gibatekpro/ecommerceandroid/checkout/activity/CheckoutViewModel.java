package com.gibatekpro.ecommerceandroid.checkout.activity;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gibatekpro.ecommerceandroid.checkout.callback.CountryCallback;
import com.gibatekpro.ecommerceandroid.checkout.callback.StateCallback;
import com.gibatekpro.ecommerceandroid.checkout.model.Country;
import com.gibatekpro.ecommerceandroid.checkout.model.State;
import com.gibatekpro.ecommerceandroid.checkout.service.CountryStateService;
import com.gibatekpro.ecommerceandroid.product.service.ProductCategoryService;

import java.util.List;

public class CheckoutViewModel extends AndroidViewModel {

    MutableLiveData<List<Country>> countryList;

    MutableLiveData<List<State>> stateList;

    public CheckoutViewModel(@NonNull Application application) {
        super(application);

        countryList = new MutableLiveData<>();

        stateList = new MutableLiveData<>();

    }


    public LiveData<List<Country>> getCountries(Activity activity) {
        fetchCountryList(activity);
        return countryList;
    }

    public LiveData<List<State>> getStates(Activity activity, String countryCode) {
        fetchStateList(activity, countryCode);
        return stateList;
    }

    private void fetchCountryList(Activity activity) {

        CountryStateService.fetchCountries(activity, new CountryCallback() {
            @Override
            public void onSuccess(List<Country> countries) {
                countryList.setValue(countries);
            }

            @Override
            public void onFailure(String error) {

            }
        });

    }

    private void fetchStateList(Activity activity, String countryCode) {

        CountryStateService.fetchStates(activity, new StateCallback() {
            @Override
            public void onSuccess(List<State> states) {
                stateList.setValue(states);
            }

            @Override
            public void onFailure(String error) {

            }
        }, countryCode);

    }

}
