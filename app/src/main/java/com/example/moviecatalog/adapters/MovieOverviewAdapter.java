package com.example.moviecatalog.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.moviecatalog.R;
import com.example.moviecatalog.models.MovieOverview;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MovieOverviewAdapter extends RecyclerView.Adapter<MovieOverviewAdapter.MovieViewHolder> {
    private final LayoutInflater inflater;
    private List<MovieOverview> movieOverviewList;
    Context context;

    private OnMovieListener onMovieListener;

    public MovieOverviewAdapter(Context context, OnMovieListener onMovieListener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.onMovieListener = onMovieListener;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView movieImageView;
        public final TextView movieTitleView;
        public final TextView movieReleaseYearView;
        public final TextView movieGenresView;
        public final TextView movieVoteAverageView;
        public final ProgressBar movieImageprogressBar;
        private final MovieOverviewAdapter adapter;
        private final OnMovieListener onMovieListener;

        public MovieViewHolder(@NonNull View itemView, MovieOverviewAdapter adapter, OnMovieListener onMovieListener) {
            super(itemView);
            movieTitleView = itemView.findViewById(R.id.movie_title_view);
            movieImageView = itemView.findViewById(R.id.movie_image_view);
            movieReleaseYearView = itemView.findViewById(R.id.movie_release_year_view);
            movieGenresView = itemView.findViewById(R.id.movie_genres_view);
            movieVoteAverageView = itemView.findViewById(R.id.movie_vote_average_view);
            movieImageprogressBar = itemView.findViewById(R.id.movie_image_progressBar);
            this.adapter = adapter;
            this.onMovieListener = onMovieListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int movieId = adapter.movieOverviewList.get(getAdapterPosition()).id;
            onMovieListener.onMovieClick(movieId);
        }
    }

    @NonNull
    @Override
    public MovieOverviewAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(itemView, this, onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieOverviewAdapter.MovieViewHolder holder, int position) {
        if(movieOverviewList != null){
            MovieOverview current = movieOverviewList.get(position);
            holder.movieTitleView.setText(current.title);

            // Gera imagem do poster do filme
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_poster_loading)
                    .error(R.drawable.ic_poster_error);
            Glide.with(this.context)
                    .load(current.posterUrl)
                    .apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.movieImageprogressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.movieImageprogressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.movieImageView);

            // Parsing da string de data de lançamento
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate releaseDate = LocalDate.parse(current.releaseDate, formatter);
            // recupera ano de lançamento
            int year = releaseDate.getYear();
            holder.movieReleaseYearView.setText(String.format(Locale.US, "%d", year));

            StringBuilder genresText = new StringBuilder();
            String prefix = "";
            for(String g : current.genres){
                genresText.append(prefix);
                prefix = ", ";
                genresText.append(g);
            }
            holder.movieGenresView.setText(genresText.toString());

            holder.movieVoteAverageView.setText(String.format(Locale.US, "%.1f/10", current.voteAverage));
        }
    }

    public void setMovieOverviewList(List<MovieOverview> list){
        movieOverviewList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(movieOverviewList != null)
            return movieOverviewList.size();
        return 0;
    }

    public interface OnMovieListener{
        void onMovieClick(int movieId);
    }
}
