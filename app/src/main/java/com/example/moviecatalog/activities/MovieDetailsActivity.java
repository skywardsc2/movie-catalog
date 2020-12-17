package com.example.moviecatalog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        int movieId = intent.getIntExtra(MainActivity.MOVIE_ID, 0);

        progressBar = binding.movieDetailsProgressBar;
        progressBar.setVisibility(View.VISIBLE);

        MoviesApiInterface moviesApi = MoviesApiService.getRetrofitInstance().create(MoviesApiInterface.class);
        moviesApi.getMovieDetails(movieId).enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    updateLayout(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.api_connection_error, Toast.LENGTH_SHORT).show();
            }
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

        binding.movieDetailsContent.detailsMovieVoteCount.setText(String.format("(%d)", movieDetails.voteCount));

        binding.movieDetailsContent.detailsMovieWebsiteText.setText(movieDetails.website);

        binding.movieDetailsContent.detailsMovieDurationText.setText(String.format("%d min", movieDetails.duration));
    }
}