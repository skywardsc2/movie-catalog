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

/*
* Activity para a lista de filmes
* */
public class MainActivity extends AppCompatActivity implements MovieOverviewAdapter.OnMovieListener {
    public static final String MOVIE_ID = "MOVIE_ID";
    private final MainActivity activity = this;

    private ActivityMainBinding viewBinding;
    private RecyclerView recyclerView;

    private ProgressBar progressBar;
    private MovieOverviewAdapter movieOverviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        progressBar = viewBinding.mainProgressBar;
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = viewBinding.mainContent.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        movieOverviewAdapter = new MovieOverviewAdapter(this, this);
        recyclerView.setAdapter(movieOverviewAdapter);

        // Gera ViewModel para a lista de filmes
        MovieOverviewListViewModel movieListViewModel = new ViewModelProvider(this, new MovieOverviewListViewModelFactory(getApplication())).get(MovieOverviewListViewModel.class);

        /*Cria Observer para o LiveData do ViewModel da lista de filmes e atualiza a
            UI de acordo*/
        movieListViewModel.getMovieOverviewList().observe(this, listResource -> {
            if(!listResource.data.isEmpty()){
                switch(listResource.status){
                    case NETWORK_ERROR:
                        Toast.makeText(activity, R.string.api_connection_error, Toast.LENGTH_LONG).show();
                        break;
                    case SERVER_ERROR:
                        Toast.makeText(activity, R.string.api_server_error, Toast.LENGTH_LONG).show();
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        movieOverviewAdapter.setMovieOverviewList(listResource.data);
                    default:
                        break;
                }
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