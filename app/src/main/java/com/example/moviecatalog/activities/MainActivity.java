package com.example.moviecatalog.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.moviecatalog.viewmodels.MovieOverviewListViewModelFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieOverviewAdapter.OnMovieListener {
    public static final String MOVIE_ID = "MOVIE_ID";
    private static final String RECYCLER_VIEW_STATE_KEY = "RECYCLER_VIEW_STATE";
    private MainActivity activity = this;

    private MovieOverviewListViewModel movieListViewModel;

    private ActivityMainBinding viewBinding;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MovieOverviewAdapter movieOverviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        progressBar = viewBinding.mainProgressBar;
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = viewBinding.mainContent.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL));

        movieListViewModel = new ViewModelProvider(this, new MovieOverviewListViewModelFactory(getApplication())).get(MovieOverviewListViewModel.class);

        movieListViewModel.getMovieOverviewList().observe(this, new Observer<Resource<List<MovieOverview>>>() {
            @Override
            public void onChanged(Resource<List<MovieOverview>> listResource) {
                Log.d("MainActivity", "onChanged: MovieList changed");
                movieOverviewAdapter = new MovieOverviewAdapter(activity, activity);
                switch(listResource.status){
                    case NETWORK_ERROR:
                        Toast.makeText(activity, R.string.api_connection_error, Toast.LENGTH_LONG).show();
                        break;
                    case SERVER_ERROR:
                        Toast.makeText(activity, "Server not responding correctly. Please, try again later!", Toast.LENGTH_LONG).show();
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        movieOverviewAdapter.setMovieOverviewList(listResource.data);
                    default:
                        break;
                }
                recyclerView.setAdapter(movieOverviewAdapter);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        int lastFirstVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(RECYCLER_VIEW_STATE_KEY, lastFirstVisiblePosition);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int lastFirstVisiblePosition = savedInstanceState.getInt(RECYCLER_VIEW_STATE_KEY);
        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
    }

    @Override
    public void onMovieClick(int movieId) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_ID, movieId);
        startActivity(intent);
    }
}