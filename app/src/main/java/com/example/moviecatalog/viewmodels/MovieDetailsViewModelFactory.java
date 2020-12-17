package com.example.moviecatalog.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieDetailsViewModelFactory implements ViewModelProvider.Factory {
    public Application application;
    private int movieId;

    public MovieDetailsViewModelFactory(Application application, int movieId){
        this.application = application;
        this.movieId = movieId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(application, movieId);
    }
}
