package com.example.diarynotesapp.api.interfaces;

import com.example.diarynotesapp.api.model.Quote;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface Api {
    String BASE_URL = "https://type.fit/api/";
    //@Headers({"Cache-Control: max-age=640000", "User-Agent: My-App-Name"})
    @GET("quotes")
    Call<List<Quote>> getQuotes();
    @GET("quotes/{id}")
    Call<Quote> getQuoteById(@Path("id") int id);
}
