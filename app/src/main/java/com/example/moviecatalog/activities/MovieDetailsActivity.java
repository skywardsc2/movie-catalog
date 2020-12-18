package com.example.moviecatalog.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.moviecatalog.R;
import com.example.moviecatalog.databinding.ActivityMovieDetailsBinding;
import com.example.moviecatalog.models.MovieDetails;
import com.example.moviecatalog.viewmodels.MovieDetailsViewModel;
import com.example.moviecatalog.viewmodels.MovieDetailsViewModelFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/*
* Activity para os detalhes de determinado filme
* */
public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding viewBinding;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        int movieId = intent.getIntExtra(MainActivity.MOVIE_ID, 0);

        progressBar = viewBinding.movieDetailsProgressBar;
//        progressBar.setVisibility(View.VISIBLE);

        // Gera ViewModel para os detalhes do filme escolhido
        MovieDetailsViewModel movieViewModel = new ViewModelProvider(this, new MovieDetailsViewModelFactory(this.getApplication(), movieId)).get(MovieDetailsViewModel.class);

        // Observa dados do filme e atualiza a UI de acordo
        movieViewModel.getMovieDetails().observe(this, (movieDetailsResource) -> {
            switch(movieDetailsResource.status){
                case NETWORK_ERROR:
                    Toast.makeText(getApplicationContext(), R.string.api_connection_error, Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.GONE);
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getApplicationContext(), R.string.api_server_error, Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    if (movieDetailsResource.data != null) {
                        updateLayout(movieDetailsResource.data);
//                        progressBar.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        });

        // Observa status do Retrofit Request para atualizar a UI da barra de progresso
        movieViewModel.getIsRetrievingData().observe(this, (isRetrievingData) -> {
            if(isRetrievingData)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        });
    }

    private void updateLayout(MovieDetails movieDetails) {
        // Recupera imagem do poster do filme
        ProgressBar imageProgressBar = viewBinding.movieDetailsContent.detailsMoviePosterBar;
        imageProgressBar.setVisibility(View.VISIBLE);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_poster_loading)
                .error(R.drawable.ic_poster_error);
        Glide.with(this)
                .load(movieDetails.posterUrl)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(viewBinding.movieDetailsContent.detailsMoviePosterView);

        viewBinding.movieDetailsContent.detailsMovieTitleView.setText(movieDetails.title);

        // Parsing da string de data de lançamento
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate releaseDate = LocalDate.parse(movieDetails.releaseDate, formatter);
        viewBinding.movieDetailsContent.detailsMovieReleaseYearView.setText(String.format(Locale.US,"%d", releaseDate.getYear()));
        // Média de votos
        viewBinding.movieDetailsContent.detailsMovieVoteAverageView.setText(String.format(Locale.US, "%.2f", movieDetails.voteAverage));
        // Contagem de votos
        viewBinding.movieDetailsContent.detailsMovieVoteCount.setText(String.format(Locale.US, "(%d)", movieDetails.voteCount));
        // Website
        viewBinding.movieDetailsContent.detailsMovieWebsiteText.setText(movieDetails.website);
        // Duração
        viewBinding.movieDetailsContent.detailsMovieDurationText.setText(String.format(Locale.US, "%d min", movieDetails.duration));

        // Generos
        StringBuilder genresText = new StringBuilder();
        String prefix = "";
        for(String g : movieDetails.genres){
            genresText.append(prefix);
            prefix = ", ";
            genresText.append(g);
        }
        viewBinding.movieDetailsContent.detailsMovieGenresText.setText(genresText.toString());
        // Resumo
        viewBinding.movieDetailsContent.detailsMovieOverviewText.setText(movieDetails.overview);
    }
}