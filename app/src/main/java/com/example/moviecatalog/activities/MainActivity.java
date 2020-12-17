package com.example.moviecatalog.activities;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moviecatalog.R;
import com.example.moviecatalog.adapters.MovieOverviewAdapter;
import com.example.moviecatalog.databinding.ActivityMainBinding;
import com.example.moviecatalog.models.MovieOverview;
import com.example.moviecatalog.network.MoviesApiInterface;
import com.example.moviecatalog.network.MoviesApiService;
import com.example.moviecatalog.network.Resource;
import com.example.moviecatalog.viewmodels.MovieOverviewListViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieOverviewAdapter.OnMovieListener {
    public static final String MOVIE_ID = "MOVIE_ID";

    private MovieOverviewListViewModel movieListViewModel;

    private ActivityMainBinding viewBinding;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private MovieOverviewAdapter movieOverviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        progressBar = viewBinding.mainProgressBar;
        progressBar.setVisibility(View.VISIBLE);

        movieOverviewAdapter = new MovieOverviewAdapter(this, this);
        recyclerView = viewBinding.mainContent.recyclerView;
        recyclerView.setAdapter(movieOverviewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        movieListViewModel = new ViewModelProvider(this).get(MovieOverviewListViewModel.class);

        movieListViewModel.getMovieOverviewList().observe(this, movieList -> {
            switch(movieList.status){
                case NETWORK_ERROR:
                    Toast.makeText(this, R.string.api_connection_error, Toast.LENGTH_LONG).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(this, "Server not responding correctly. Please, try again later!", Toast.LENGTH_LONG).show();
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    movieOverviewAdapter.setMovieOverviewList(movieList.data);
                default:
                    break;
            }
        });
    }

    @Override
    public void onMovieClick(int movieId) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_ID, movieId);
        startActivity(intent);
    }
}