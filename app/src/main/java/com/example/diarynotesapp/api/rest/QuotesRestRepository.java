package com.example.diarynotesapp.api.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.diarynotesapp.api.interfaces.Api;
import com.example.diarynotesapp.api.model.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuotesRestRepository {
    private static QuotesRestRepository instance = null;
    private Api api;
    private QuotesRestRepository() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }
    public static synchronized QuotesRestRepository getInstance() {
        if (instance == null) {
            instance = new QuotesRestRepository();
        }

        return instance;
    }
    public Api getMyApi() {
        return api;
    }


}
