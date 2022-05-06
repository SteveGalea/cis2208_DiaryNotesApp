package com.example.diarynotesapp.api.interfaces;

import com.example.diarynotesapp.api.model.Quote;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "https://type.fit/api/";
    @GET("quotes")
    Call<List<Quote>> getQuotes();
    //Call<Quote> getQuoteById(int id);
}
