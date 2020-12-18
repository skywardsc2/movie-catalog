package com.example.moviecatalog.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviecatalog.DataRepository;
import com.example.moviecatalog.models.MovieDetails;
import com.example.moviecatalog.models.MovieOverview;
import com.example.moviecatalog.network.Resource;

import java.util.List;

public class MovieDetailsViewModel extends ViewModel {
    private DataRepository repository;
    private LiveData<Boolean> observableIsRetrievingData;
    private LiveData<Resource<MovieDetails>> observableMovieDetails;

    public MovieDetailsViewModel(Application application, int movieId){
        repository = DataRepository.getInstance();
        observableMovieDetails = repository.getMovieDetails(movieId);
        observableIsRetrievingData = repository.getStatus();
    }

    public LiveData<Resource<MovieDetails>> getMovieDetails() {
        return observableMovieDetails;
    }

    public LiveData<Boolean> getIsRetrievingData() {
        return observableIsRetrievingData;
    }
}
