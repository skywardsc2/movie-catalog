package com.example.moviecatalog.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
//import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moviecatalog.R;
import com.example.moviecatalog.adapters.MovieOverviewAdapter;
import com.example.moviecatalog.databinding.ActivityMainBinding;
import com.example.moviecatalog.models.MovieOverview;
import com.example.moviecatalog.network.MoviesApiInterface;
import com.example.moviecatalog.network.MoviesApiService;
import com.example.moviecatalog.viewmodels.MovieListViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviecatalog.viewmodels.MovieListViewModel.MOVIE_ID;

public class MainActivity extends AppCompatActivity implements MovieOverviewAdapter.OnMovieListener {
    public static final String MOVIE_ID = "MOVIE_ID";
    private List<MovieOverview> movieOverviewList;

    private ActivityMainBinding viewBinding;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private MovieOverviewAdapter movieOverviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

//        movieListViewModel = ViewModelProvider(this).get(MovieListViewModel.class);
//        movieListViewModel.

        progressBar = viewBinding.mainProgressBar;
        progressBar.setVisibility(View.VISIBLE);

        MoviesApiInterface moviesApi = MoviesApiService.getRetrofitInstance().create(MoviesApiInterface.class);
        moviesApi.getMovieList().enqueue(new Callback<List<MovieOverview>>() {
            @Override
            public void onResponse(Call<List<MovieOverview>> call, Response<List<MovieOverview>> response) {
                progressBar.setVisibility(View.GONE);
                movieOverviewList = response.body();
                updateMovieListView();
            }

            @Override
            public void onFailure(Call<List<MovieOverview>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.api_connection_error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateMovieListView(){
        recyclerView = viewBinding.mainContent.recyclerView;
        movieOverviewAdapter = new MovieOverviewAdapter(this, movieOverviewList, this);
        recyclerView.setAdapter(movieOverviewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_ID, movieOverviewList.get(position).id);
        startActivity(intent);
    }
}