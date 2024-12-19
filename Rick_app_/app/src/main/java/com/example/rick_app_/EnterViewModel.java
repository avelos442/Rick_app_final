package com.example.rick_app_;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class EnterViewModel {
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private final SharedPreferences sharedPreferences;

    public EnterViewModel(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        boolean loggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        isLoggedIn.setValue(loggedIn);
    }

    public LiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }
}

