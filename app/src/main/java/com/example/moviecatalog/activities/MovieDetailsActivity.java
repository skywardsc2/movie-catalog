package com.example.moviecatalog.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviecatalog.R;
import com.example.moviecatalog.databinding.ActivityMovieDetailsBinding;
import com.example.moviecatalog.databinding.ContentMovieDetailsBinding;
import com.example.moviecatalog.models.MovieDetails;
import com.example.moviecatalog.models.MovieOverview;
import com.example.moviecatalog.network.MoviesApiInterface;
import com.example.moviecatalog.network.MoviesApiService;
import com.example.moviecatalog.network.Resource;
import com.example.moviecatalog.viewmodels.MovieDetailsViewModel;
import com.example.moviecatalog.viewmodels.MovieDetailsViewModelFactory;
import com.example.moviecatalog.viewmodels.MovieOverviewListViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private MovieDetailsViewModel movieViewModel;

    private ActivityMovieDetailsBinding binding;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Details", "onCreate: details Activity");
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        int movieId = intent.getIntExtra(MainActivity.MOVIE_ID, 0);

        progressBar = binding.movieDetailsProgressBar;
        progressBar.bringToFront();
        progressBar.setVisibility(View.VISIBLE);

        movieViewModel = new ViewModelProvider(this, new MovieDetailsViewModelFactory(this.getApplication(), movieId)).get(MovieDetailsViewModel.class);

        movieViewModel.getMovieDetails().observe(this, movieDetailsResource -> {

                switch(movieDetailsResource.status){
                    case NETWORK_ERROR:
                        Toast.makeText(getApplicationContext(), R.string.api_connection_error, Toast.LENGTH_LONG).show();
                        break;
                    case SERVER_ERROR:
                        Toast.makeText(getApplicationContext(), R.string.api_server_error, Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        if (movieDetailsResource.data != null) {
                            updateLayout(movieDetailsResource.data);
                        }
                        break;
                    default:
                        break;
                }
                progressBar.setVisibility(View.GONE);
            });
    }

    private void updateLayout(MovieDetails movieDetails) {
        // Gera imagem do poster do filme
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_poster_loading)
                .error(R.drawable.ic_poster_error);
        Glide.with(this).load(movieDetails.posterUrl).apply(options).into(binding.movieDetailsContent.detailsMoviePosterView);

        binding.movieDetailsContent.detailsMovieTitleView.setText(movieDetails.title);

        // Parsing da string de data de lançamento
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate releaseDate = LocalDate.parse(movieDetails.releaseDate, formatter);
        binding.movieDetailsContent.detailsMovieReleaseYearView.setText(String.format(Locale.US,"%d", releaseDate.getYear()));

        binding.movieDetailsContent.detailsMovieVoteAverageView.setText(String.format(Locale.US, "%.2f", movieDetails.voteAverage));

        binding.movieDetailsContent.detailsMovieVoteCount.setText(String.format(Locale.US, "(%d)", movieDetails.voteCount));

        binding.movieDetailsContent.detailsMovieWebsiteText.setText(movieDetails.website);

        binding.movieDetailsContent.detailsMovieDurationText.setText(String.format(Locale.US, "%d min", movieDetails.duration));

        StringBuilder genresText = new StringBuilder();
        String prefix = "";
        for(String g : movieDetails.genres){
            genresText.append(prefix);
            prefix = ", ";
            genresText.append(g);
        }
        binding.movieDetailsContent.detailsMovieGenresText.setText(genresText.toString());

        binding.movieDetailsContent.detailsMovieOverviewText.setText(movieDetails.overview);
    }
}