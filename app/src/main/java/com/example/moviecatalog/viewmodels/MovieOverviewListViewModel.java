package com.example.moviecatalog.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviecatalog.DataRepository;
import com.example.moviecatalog.models.MovieOverview;
import com.example.moviecatalog.network.Resource;

import java.util.List;

public class MovieOverviewListViewModel extends ViewModel {
    private static final String TAG = MovieOverviewListViewModel.class.getName();
    private LiveData<Resource<List<MovieOverview>>> observableMovieOverviewList;

    private DataRepository repository = DataRepository.getInstance();

    public MovieOverviewListViewModel(){
        observableMovieOverviewList = repository.getMovieOverviewList();
    }

    public LiveData<Resource<List<MovieOverview>>> getMovieOverviewList() {
        return observableMovieOverviewList;
    }
}
