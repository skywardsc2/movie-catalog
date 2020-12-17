package com.example.moviecatalog.network;

import com.squareup.moshi.Moshi;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MoviesApiService {
    private static final String BASE_URL = "https://desafio-mobile.nyc3.digitaloceanspaces.com";
    private static Retrofit retrofit;
    private static Moshi moshi;

    public static Retrofit getRetrofitInstance(){
        if(moshi == null) {
            moshi = new Moshi.Builder()
                    .build();
        }
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();
        }
        return retrofit;
    }
}