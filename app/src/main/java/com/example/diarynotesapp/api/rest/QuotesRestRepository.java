package com.example.diarynotesapp.api.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.diarynotesapp.api.interfaces.Api;
import com.example.diarynotesapp.api.model.Quote;

import java.util.List;
import java.util.Random;

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
    public LiveData<Quote> fetchQuote() {
        final MutableLiveData<Quote> quote = new MutableLiveData<>();
        api.getQuotes().enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(@NonNull Call<List<Quote>> call, @NonNull
                    Response<List<Quote>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<Quote> quotes = response.body(); // get all quotes
                /*for(Quote quoteX :quotes){
                    System.out.println(quoteX); // randomize
                    quote.setValue(quoteX);
                }*/
                Random rand = new Random();
                int randInt = rand.nextInt(quotes.size());
                Quote random = quotes.get(randInt);
                quote.setValue(random);
            }
            @Override
            public void onFailure(@NonNull Call<List<Quote>> call, @NonNull
                    Throwable t) {
                Log.i("fetchQuotes", call.request().toString());
                Log.e("fetchQuotes", t.getMessage());
                quote.setValue(null);
            }
        });
        return quote;
    }
}
