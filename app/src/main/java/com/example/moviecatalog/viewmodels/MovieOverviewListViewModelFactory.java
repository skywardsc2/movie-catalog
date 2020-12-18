package com.example.moviecatalog.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviecatalog.models.MovieOverview;

public class MovieOverviewListViewModelFactory implements ViewModelProvider.Factory {
    public Application application;

    public MovieOverviewListViewModelFactory(Application application){
        this.application = application;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieOverviewListViewModel(application);
    }
}
