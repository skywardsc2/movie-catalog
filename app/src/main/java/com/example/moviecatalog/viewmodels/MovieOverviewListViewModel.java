package com.example.moviecatalog.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviecatalog.DataRepository;
import com.example.moviecatalog.models.MovieOverview;
import com.example.moviecatalog.network.Resource;

import java.util.List;

public class MovieOverviewListViewModel extends ViewModel {
    private LiveData<Resource<List<MovieOverview>>> observableMovieOverviewList;

    private DataRepository repository = DataRepository.getInstance();

    public MovieOverviewListViewModel(Application application){
        observableMovieOverviewList = repository.getMovieOverviewList();
    }

    public LiveData<Resource<List<MovieOverview>>> getMovieOverviewList() {
        return observableMovieOverviewList;
    }
}
