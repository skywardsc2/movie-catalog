package com.example.moviecatalog;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviecatalog.models.MovieDetails;
import com.example.moviecatalog.models.MovieOverview;
import com.example.moviecatalog.network.MoviesApiInterface;
import com.example.moviecatalog.network.MoviesApiService;
import com.example.moviecatalog.network.Resource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {
    private static DataRepository instance;

    private MutableLiveData<Resource<List<MovieOverview>>> movieOverviewList = new MutableLiveData<>();
    private MutableLiveData<Resource<MovieDetails>> movieDetails = new MutableLiveData<>();

    private DataRepository(){ }

    public static DataRepository getInstance() {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Resource<List<MovieOverview>>> getMovieOverviewList() {
        MoviesApiInterface moviesApi = MoviesApiService.getRetrofitInstance().create(MoviesApiInterface.class);
        moviesApi.getMovieList().enqueue(new Callback<List<MovieOverview>>() {
            @Override
            public void onResponse(Call<List<MovieOverview>> call, Response<List<MovieOverview>> response) {
                if(response.body() != null)
                    movieOverviewList.setValue(Resource.success(response.body()));
            }
            @Override
            public void onFailure(Call<List<MovieOverview>> call, Throwable t) {
                if(t instanceof IOException)
                    movieOverviewList.setValue(Resource.network_error(t.getMessage(), null));
                else
                    movieOverviewList.setValue(Resource.server_error(t.getMessage(), null));
            }
        });

        return movieOverviewList;
    }

    public LiveData<Resource<MovieDetails>> getMovieDetails(int movieId) {
        MoviesApiInterface moviesApi = MoviesApiService.getRetrofitInstance().create(MoviesApiInterface.class);
        moviesApi.getMovieDetails(movieId).enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if(response.body() != null){
                    movieDetails.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                if(t instanceof IOException)
                    movieDetails.setValue(Resource.network_error(t.getMessage(), null));
                else
                    movieDetails.setValue(Resource.server_error(t.getMessage(), null));
            }
        });

        return movieDetails;
    }
}
