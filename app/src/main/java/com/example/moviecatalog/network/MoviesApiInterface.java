package com.example.moviecatalog.network;

import com.example.moviecatalog.models.MovieDetails;
import com.example.moviecatalog.models.MovieOverview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesApiInterface {
    @GET("movies")
    Call<List<MovieOverview>> getMovieList();

    @GET("movies/{id}")
    Call<MovieDetails> getMovieDetails(@Path("id") int id);
}
